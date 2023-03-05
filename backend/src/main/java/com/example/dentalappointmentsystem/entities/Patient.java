package com.example.dentalappointmentsystem.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Entity
@Table(name = "patients")
@NoArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "patient_name")
    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String email;

    private String address;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;
}
