package com.das.entities;

import com.das.validators.EnumValidator;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

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

    @Column(name = "a_date", nullable = false)
    @NotNull(message = "Date cannot be null")
    private Date date;

    @Column(name = "a_time", nullable = false)
    @NotNull(message = "Time cannot be null")
    private Time time;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "a_status", nullable = false)
    @NotNull(message = "Status cannot be null")
    @EnumValidator(targetClassType = Status.class, message = "Invalid status")
    private Status status;

}
