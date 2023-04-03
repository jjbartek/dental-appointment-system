package com.das.DTOs;

import com.das.entities.Patient;
import com.das.entities.Service;
import com.das.entities.Status;
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
    private Status status;
    private List<Service> services;
    private BigDecimal total;
}
