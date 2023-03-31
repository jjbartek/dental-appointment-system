package com.das.entities;

import com.das.config.AppConstants;
import com.das.validators.EnumValidator;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "appointments")
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    @NotNull(message = "Employee cannot be null")
    private User employee;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    @NotNull(message = "Patient cannot be null")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    @NotNull(message = "Service type cannot be null")
    private Service service;

    @Column(columnDefinition = "DECIMAL(6, 2)")
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 4, fraction = 2)
    private BigDecimal total;

    @Column(name = "startTime", nullable = false, columnDefinition = "TIMESTAMP")
    @NotNull(message = "Start time cannot be null")
    @JsonFormat(pattern = AppConstants.DATE_FORMAT, timezone = AppConstants.TIME_ZONE)
    private LocalDateTime startTime;

    @Column(name = "endTime", nullable = false, columnDefinition = "TIMESTAMP")
    @NotNull(message = "End time cannot be null")
    @JsonFormat(pattern = AppConstants.DATE_FORMAT, timezone = AppConstants.TIME_ZONE)
    private LocalDateTime endTime;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "a_status", nullable = false)
    @NotNull(message = "Status cannot be null")
    @EnumValidator(targetClassType = Status.class, message = "Invalid status")
    private Status status;

}
