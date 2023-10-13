package com.das.appointments;

import com.das.appointments.DTOs.AppointmentDTO;
import com.das.appointments.DTOs.SimplifiedAppointmentDTO;
import com.das.appointments.entities.*;
import com.das.appointments.exceptions.AppointmentTimeNotAvailable;
import com.das.common.exceptions.ResourceNotFoundException;
import com.das.users.UserNotAuthorizedException;
import com.das.patients.Patient;
import com.das.patients.PatientRepository;
import com.das.services.ServiceRepository;
import com.das.users.entities.UserRole;
import com.das.users.entities.User;
import com.das.users.UserRepository;
import com.das.common.responses.CollectionResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public CollectionResponse<SimplifiedAppointmentDTO> getAppointmentsByEmployeeId(Integer id, LocalDateTime dateTime, boolean showPreceding, Pageable pageable) {
        if(dateTime == null) dateTime = LocalDateTime.now();

        Page<Appointment> page;
        if(showPreceding) {
            page = appointmentRepository.findByPreTimeAndEmployeeId(dateTime, id, pageable);
        } else {
            page = appointmentRepository.findByPostTimeAndEmployeeId(dateTime, id, pageable);
        }

        return buildAppointmentResponse(page);
    }

    public CollectionResponse<SimplifiedAppointmentDTO> getAppointmentsByPatientId(Integer id, LocalDateTime dateTime, boolean showPreceding, Pageable pageable) {
        if(dateTime == null) dateTime = LocalDateTime.now();

        Page<Appointment> page;
        if(showPreceding) {
            page = appointmentRepository.findByPreTimeAndPatientId(dateTime, id, pageable);
        } else {
            page = appointmentRepository.findByPostTimeAndPatientId(dateTime, id, pageable);
        }

        return buildAppointmentResponse(page);
    }

    public CollectionResponse<SimplifiedAppointmentDTO> getAppointments(LocalDateTime dateTime, boolean showPreceding, Pageable pageable) {
        if(dateTime == null) dateTime = LocalDateTime.now();

        Page<Appointment> page;
        if(showPreceding) {
            page = appointmentRepository.findByPreTime(dateTime, pageable);
        } else {
            page = appointmentRepository.findByPostTime(dateTime, pageable);
        }

        return buildAppointmentResponse(page);
    }

    public AppointmentDTO getAppointmentById(Integer id) {
        return modelMapper.map(getAppointmentOrThrow(id), AppointmentDTO.class);
    }

    @Transactional
    public AppointmentDTO addAppointment(AppointmentRequest appointmentRequest) {
        Appointment appointment = new Appointment();
        saveDataToAppointment(appointment, appointmentRequest);
        appointmentRepository.save(appointment);

        return modelMapper.map(appointment, AppointmentDTO.class);
    }

    @Transactional
    public AppointmentDTO updateAppointment(Integer id, AppointmentRequest appointmentRequest) {
        Appointment appointment = getAppointmentOrThrow(id);
        saveDataToAppointment(appointment, appointmentRequest);
        appointmentRepository.save(appointment);

        return modelMapper.map(appointment, AppointmentDTO.class);
    }

    @Transactional
    public void deleteAppointment(Integer id) {
        Appointment appointment = getAppointmentOrThrow(id);

        appointmentRepository.delete(appointment);
    }

    private Appointment getAppointmentOrThrow(Integer id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "appointment id", id));
    }

    private boolean isAppointmentTimeNotAvailable(Appointment appointment) {
        List<Appointment> interferingAppointments = appointmentRepository.findByTimeBetweenAndEmployeeId(
                appointment.getStartTime(),
                appointment.getEndTime(),
                appointment.getEmployee().getId()
        );

        return interferingAppointments.size() > 0 && !interferingAppointments.get(0).getId().equals(appointment.getId());
    }

    private CollectionResponse<SimplifiedAppointmentDTO> buildAppointmentResponse(Page<Appointment> page) {
        List<SimplifiedAppointmentDTO> appointments = page.getContent()
                .stream()
                .map(appointment -> modelMapper.map(appointment, SimplifiedAppointmentDTO.class))
                .toList();

        return CollectionResponse.<SimplifiedAppointmentDTO>builder()
                .content(appointments)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .lastPage(page.isLast())
                .build();
    }

    private void saveDataToAppointment(Appointment appointment, AppointmentRequest appointmentRequest) {
        List<com.das.services.Service> services = new ArrayList<>();

        for(Integer id : appointmentRequest.getServiceIds()) {
            com.das.services.Service service = serviceRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Service", "service id", id));

            services.add(service);
        }

        User employee = userRepository.findById(appointmentRequest.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "employee id", appointmentRequest.getEmployeeId()));
        Patient patient = patientRepository.findById(appointmentRequest.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "patient id", appointmentRequest.getPatientId()));

        if(!employee.hasRole(UserRole.EMPLOYEE))
            throw new UserNotAuthorizedException(employee.getId(), "carry out services");

        appointment.setServices(services);
        appointment.setEmployee(employee);
        appointment.setPatient(patient);
        appointment.setStartTime(appointmentRequest.getStartTime());
        appointment.setEndTime(appointmentRequest.getEndTime());
        appointment.setNotes(appointmentRequest.getNotes());
        appointment.setStatus(AppointmentStatus.valueOf(appointmentRequest.getStatus()));

        if(isAppointmentTimeNotAvailable(appointment))
            throw new AppointmentTimeNotAvailable(appointment.getStartTime());
    }

}
