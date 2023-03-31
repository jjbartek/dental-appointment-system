package com.das.services;

import com.das.entities.Appointment;
import com.das.entities.Role;
import com.das.exceptions.AppointmentTimeNotAvailable;
import com.das.exceptions.ResourceNotFoundException;
import com.das.exceptions.UserDoesNotHavePrivilegeException;
import com.das.repositories.AppointmentRepository;
import com.das.repositories.PatientRepository;
import com.das.responses.CollectionResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;

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
    public Appointment addAppointment(Appointment appointment) {
        appointment.setId(null);

        if(!appointment.getEmployee().hasRole(Role.EMPLOYEE))
            throw new UserDoesNotHavePrivilegeException(appointment.getEmployee().getId(), "carry out services");

        if(!patientRepository.existsById(appointment.getPatient().getId()))
            throw new ResourceNotFoundException("Patient", "patient id", appointment.getPatient().getId());

        if(isAppointmentTimeNotAvailable(appointment))
            throw new AppointmentTimeNotAvailable(appointment.getStartTime());

        return appointmentRepository.save(appointment);
    }

    public Appointment updateAppointment(Integer id, Appointment updatedAppointment) {
        Appointment appointment = getAppointmentOrThrow(id);

        if(!appointment.getEmployee().hasRole(Role.EMPLOYEE))
            throw new UserDoesNotHavePrivilegeException(appointment.getEmployee().getId(), "carry out services");

        if(isAppointmentTimeNotAvailable(updatedAppointment))
            throw new AppointmentTimeNotAvailable(appointment.getStartTime());

        appointment.setEmployee(updatedAppointment.getEmployee());
        appointment.setPatient(updatedAppointment.getPatient());
        appointment.setService(updatedAppointment.getService());
        appointment.setTotal(updatedAppointment.getTotal());
        appointment.setStartTime(updatedAppointment.getStartTime());
        appointment.setEndTime(updatedAppointment.getEndTime());
        appointment.setNotes(updatedAppointment.getNotes());
        appointment.setStatus(updatedAppointment.getStatus());

        return appointmentRepository.save(appointment);
    }
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
}
