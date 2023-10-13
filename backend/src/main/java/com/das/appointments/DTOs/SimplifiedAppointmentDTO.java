package com.das.appointments.DTOs;

import com.das.appointments.entities.AppointmentStatus;
import com.das.patients.SimplifiedPatientDTO;
import com.das.users.DTOs.SimplifiedUserDTO;
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
    private AppointmentStatus status;
}
