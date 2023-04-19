package com.das.DTOs;

import com.das.entities.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimplifiedAppointmentDTO {
    private Integer id;
    private SimplifiedUserDTO employee;
    private SimplifiedPatientDTO patient;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Status status;
}
