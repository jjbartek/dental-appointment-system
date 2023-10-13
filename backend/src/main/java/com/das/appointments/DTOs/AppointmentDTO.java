package com.das.appointments.DTOs;

import com.das.appointments.entities.AppointmentStatus;
import com.das.users.DTOs.UserDTO;
import com.das.patients.Patient;
import com.das.services.Service;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {
    private Integer id;
    private UserDTO employee;
    private Patient patient;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String notes;
    private AppointmentStatus status;
    private List<Service> services;
    private BigDecimal total;
}
