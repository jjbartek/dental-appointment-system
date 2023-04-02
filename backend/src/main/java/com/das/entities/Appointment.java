package com.das.entities;

import com.das.config.AppConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
    private Status status;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "appointments_services",
            joinColumns = @JoinColumn(name = "appointment_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    @NotNull(message = "List of services cannot be null")
    @NotEmpty(message = "List of services must have at least one service")
    private List<Service> services;

}
