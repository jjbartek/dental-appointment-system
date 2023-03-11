package com.das.entities;

import com.das.validators.PastDate;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Entity
@Table(name = "patients")
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "patient_name", nullable = false)
    @NotBlank(message = "Patient name cannot be empty")
    @NotNull(message = "Patient name cannot be null")
    private String name;

    @Column(name = "phone_number", nullable = false)
    @NotNull(message = "Number cannot be null")
    @Pattern(regexp = "^\\d{9}$", message = "Invalid number")
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    @NotNull(message = "Email cannot be null")
    @Email(message = "Invalid email")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Address cannot be empty")
    @NotNull(message = "Address cannot be null")
    private String address;

    @Column(name = "date_of_birth", nullable = false)
    @NotNull(message = "Date of birth cannot be null")
    @PastDate(message = "Invalid date of birth")
    private Date dateOfBirth;
}
