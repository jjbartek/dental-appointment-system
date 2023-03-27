package com.das.services;

import com.das.entities.Appointment;
import com.das.entities.Role;
import com.das.entities.Status;
import com.das.exceptions.AppointmentTimeNotAvailable;
import com.das.exceptions.ResourceNotFoundException;
import com.das.exceptions.UserDoesNotHavePrivilegeException;
import com.das.repositories.AppointmentRepository;
import com.das.repositories.UserRepository;
import com.das.responses.CollectionResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    public CollectionResponse<Appointment> getAppointmentsByEmployeeId(Integer id, Integer pageNumber, Integer pageSize, Date date, Time time) {
        fillDateAndTimeIfNull(date, time);

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Appointment> page = appointmentRepository.findByDateAndTimeAndEmployeeId(date, time, id, pageable);

        return buildAppointmentResponse(page);
    }

    public CollectionResponse<Appointment> getAppointmentsByPatientId(Integer id, Integer pageNumber, Integer pageSize, Date date, Time time) {
        fillDateAndTimeIfNull(date, time);

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Appointment> page = appointmentRepository.findByDateAndTimeAndPatientId(date, time, id, pageable);

        return buildAppointmentResponse(page);
    }

    public CollectionResponse<Appointment> getAppointments(Integer pageNumber, Integer pageSize, Date date, Time time) {
        fillDateAndTimeIfNull(date, time);

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Appointment> page = appointmentRepository.findByDateAndTime(date, time, pageable);

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

            // Validate that patient exists

        if(isAppointmentTimeNotAvailable(appointment))
            throw new AppointmentTimeNotAvailable(appointment.getDate(), appointment.getTime());

        return appointmentRepository.save(appointment);
    }

    public Appointment updateAppointment(Integer id, Appointment updatedAppointment) {
        Appointment appointment = getAppointmentOrThrow(id);

        if(!appointment.getEmployee().hasRole(Role.EMPLOYEE))
            throw new UserDoesNotHavePrivilegeException(appointment.getEmployee().getId(), "carry out services");

        if(isAppointmentTimeNotAvailable(updatedAppointment))
            throw new AppointmentTimeNotAvailable(updatedAppointment.getDate(), updatedAppointment.getTime());

        appointment.setEmployee(updatedAppointment.getEmployee());
        appointment.setPatient(updatedAppointment.getPatient());
        appointment.setService(updatedAppointment.getService());
        appointment.setTotal(updatedAppointment.getTotal());
        appointment.setDate(updatedAppointment.getDate());
        appointment.setTime(updatedAppointment.getTime());
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
        //find previous
        Optional<Appointment> optionalPrevAppointment = appointmentRepository.findFirstByDateAndTimeBeforeAndEmployeeIdOrderByTimeDesc(
                appointment.getDate(),
                appointment.getTime(),
                appointment.getEmployee().getId()
        );

        if (optionalPrevAppointment.isPresent()) {
            Appointment prevAppointment = optionalPrevAppointment.get();
            LocalTime prevAppointmentLocalTime = prevAppointment.getTime().toLocalTime();
            LocalTime prevAppointmentEndTime = prevAppointmentLocalTime.plusMinutes(prevAppointment.getService().getDuration());
            // previous appointment ends during requested
            if (prevAppointmentEndTime.isAfter(appointment.getTime().toLocalTime())
                    && !prevAppointment.getId().equals(appointment.getId())
                    && !prevAppointment.getStatus().equals(Status.CANCELED)) {
                return true;
            }
        }

        Time endTime = Time.valueOf(appointment.getTime().toLocalTime().plusMinutes(appointment.getService().getDuration()));
        Optional<Appointment> optionalNextAppointment = appointmentRepository.findFirstByDateAndTimeBetweenAndEmployeeIdOrderByTimeAsc(
                appointment.getDate(),
                appointment.getTime(),
                endTime,
                appointment.getEmployee().getId()
        );

        if (optionalNextAppointment.isPresent()) {
            Appointment nextAppointment = optionalNextAppointment.get();
            LocalTime requestedAppointmentLocalTime = appointment.getTime().toLocalTime();
            LocalTime requestedAppointmentEndTime = requestedAppointmentLocalTime.plusMinutes(appointment.getService().getDuration());

            // next appointment starts before requested ends
            return requestedAppointmentEndTime.isAfter(nextAppointment.getTime().toLocalTime())
                    && !nextAppointment.getId().equals(appointment.getId())
                    && !nextAppointment.getStatus().equals(Status.CANCELED);
        }

        return false;
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

    private void fillDateAndTimeIfNull(Date date, Time time) {
        if (date == null) date = new Date(System.currentTimeMillis());
        if (time == null) time = new Time(System.currentTimeMillis());
    }
}
