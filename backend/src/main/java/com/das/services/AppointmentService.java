package com.das.services;

import com.das.entities.Appointment;
import com.das.entities.Patient;
import com.das.entities.Role;
import com.das.entities.User;
import com.das.exceptions.AppointmentTimeNotAvailable;
import com.das.exceptions.ResourceNotFoundException;
import com.das.exceptions.UserDoesNotHavePrivilegeException;
import com.das.payloads.AppointmentRequest;
import com.das.repositories.AppointmentRepository;
import com.das.repositories.PatientRepository;
import com.das.repositories.ServiceRepository;
import com.das.repositories.UserRepository;
import com.das.responses.CollectionResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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

    public CollectionResponse<Appointment> getAppointmentsByEmployeeId(Integer id, LocalDateTime dateTime, boolean showPreceding, Pageable pageable) {
        if(dateTime == null) dateTime = LocalDateTime.now();

        Page<Appointment> page;
        if(showPreceding) {
            page = appointmentRepository.findByPreTimeAndEmployeeId(dateTime, id, pageable);
        } else {
            page = appointmentRepository.findByPostTimeAndEmployeeId(dateTime, id, pageable);
        }

        return buildAppointmentResponse(page);
    }

    public CollectionResponse<Appointment> getAppointmentsByPatientId(Integer id, LocalDateTime dateTime, boolean showPreceding, Pageable pageable) {
        if(dateTime == null) dateTime = LocalDateTime.now();

        Page<Appointment> page;
        if(showPreceding) {
            page = appointmentRepository.findByPreTimeAndPatientId(dateTime, id, pageable);
        } else {
            page = appointmentRepository.findByPostTimeAndPatientId(dateTime, id, pageable);
        }

        return buildAppointmentResponse(page);
    }

    public CollectionResponse<Appointment> getAppointments(LocalDateTime dateTime, boolean showPreceding, Pageable pageable) {
        if(dateTime == null) dateTime = LocalDateTime.now();

        Page<Appointment> page;
        if(showPreceding) {
            page = appointmentRepository.findByPreTime(dateTime, pageable);
        } else {
            page = appointmentRepository.findByPostTime(dateTime, pageable);
        }

        return buildAppointmentResponse(page);
    }

    public Appointment getAppointmentById(Integer id) {
        return getAppointmentOrThrow(id);
    }

    @Transactional
    public Appointment addAppointment(AppointmentRequest appointmentRequest) {
        Appointment appointment = new Appointment();
        saveDataToAppointment(appointment, appointmentRequest);

        return appointmentRepository.save(appointment);
    }

    @Transactional
    public Appointment updateAppointment(Integer id, AppointmentRequest appointmentRequest) {
        Appointment appointment = getAppointmentOrThrow(id);
        saveDataToAppointment(appointment, appointmentRequest);

        return appointmentRepository.save(appointment);
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

    private CollectionResponse<Appointment> buildAppointmentResponse(Page<Appointment> page) {
        return CollectionResponse.<Appointment>builder()
                .content(page.getContent())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .lastPage(page.isLast())
                .build();
    }

    private void saveDataToAppointment(Appointment appointment, AppointmentRequest appointmentRequest) {
        List<com.das.entities.Service> services = new ArrayList<>();

        for(Integer id : appointmentRequest.getServiceIds()) {
            com.das.entities.Service service = serviceRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Service", "service id", id));

            services.add(service);
        }

        User employee = userRepository.findById(appointmentRequest.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "employee id", appointmentRequest.getEmployeeId()));
        Patient patient = patientRepository.findById(appointmentRequest.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "patient id", appointmentRequest.getPatientId()));

        if(!employee.hasRole(Role.EMPLOYEE))
            throw new UserDoesNotHavePrivilegeException(employee.getId(), "carry out services");

        appointment.setServices(services);
        appointment.setEmployee(employee);
        appointment.setPatient(patient);
        appointment.setStartTime(appointmentRequest.getStartTime());
        appointment.setEndTime(appointmentRequest.getEndTime());
        appointment.setNotes(appointmentRequest.getNotes());
        appointment.setStatus(appointmentRequest.getStatus());

        if(isAppointmentTimeNotAvailable(appointment))
            throw new AppointmentTimeNotAvailable(appointment.getStartTime());
    }

}
