package com.das.services;

import com.das.entities.Appointment;
import com.das.repositories.AppointmentRepository;
import com.das.responses.AppointmentResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final ModelMapper modelMapper;

    public AppointmentResponse getAppointmentsByEmployeeId(Integer id, Integer pageNumber, Integer pageSize, Date date, Time time) {
        if (date == null) date = new Date(System.currentTimeMillis());
        if (time == null) time = new Time(System.currentTimeMillis());

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Appointment> page = appointmentRepository.findByDateAndTimeAndEmployeeId(date, time, id, pageable);

        return AppointmentResponse.builder()
                .content(page.getContent())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .lastPage(page.isLast())
                .build();
    }
}
