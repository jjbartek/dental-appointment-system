package com.das.appointments.entities;

import com.das.common.constants.CommonConstants;
import com.das.patients.Patient;
import com.das.services.Service;
import com.das.users.entities.User;
import com.das.common.entities.TimeFrame;
import com.das.appointments.validators.ValidateFollowingDates;
import com.das.common.validators.ValidateLimitedTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "appointments")
@NoArgsConstructor
@AllArgsConstructor
@ValidateFollowingDates(message = "Start-time has to be before end-time")
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

    @Column(name = "start_time", nullable = false, columnDefinition = "TIMESTAMP")
    @NotNull(message = "Start time cannot be null")
    @JsonFormat(pattern = CommonConstants.DATE_FORMAT, timezone = CommonConstants.TIME_ZONE)
    @ValidateLimitedTime(timeframe = TimeFrame.FUTURE, message = "Start of the visit have to be in the future")
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false, columnDefinition = "TIMESTAMP")
    @NotNull(message = "End time cannot be null")
    @JsonFormat(pattern = CommonConstants.DATE_FORMAT, timezone = CommonConstants.TIME_ZONE)
    @ValidateLimitedTime(timeframe = TimeFrame.FUTURE, message = "End of the visit have to be in the future")
    private LocalDateTime endTime;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "a_status", nullable = false)
    @NotNull(message = "Status cannot be null")
    private AppointmentStatus status;

    @ManyToMany
    @JoinTable(
            name = "appointments_services",
            joinColumns = @JoinColumn(name = "appointment_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    @NotEmpty(message = "List of services must have at least one service")
    private List<Service> services;

    @Column(columnDefinition = "DECIMAL(8, 2)")
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 6, fraction = 2)
    private BigDecimal total;

    public Appointment(User employee, Patient patient, LocalDateTime startTime, LocalDateTime endTime, String notes, AppointmentStatus status, List<Service> services, BigDecimal total) {
        this.employee = employee;
        this.patient = patient;
        this.startTime = startTime;
        this.endTime = endTime;
        this.notes = notes;
        this.status = status;
        this.services = services;
        this.total = total;
    }
}
