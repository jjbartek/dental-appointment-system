package com.example.dentalappointmentsystem.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;

@Data
@Entity
@Table(name = "appointments")
@NoArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private User employee;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    private float total;

    @Column(name = "a_date")
    private Date date;

    @Column(name = "a_time")
    private Time time;

    private String notes;

}
