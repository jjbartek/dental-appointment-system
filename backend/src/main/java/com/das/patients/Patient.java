package com.das.patients;

import com.das.common.constants.CommonConstants;
import com.das.common.controllers.Gender;
import com.das.common.entities.TimeFrame;
import com.das.common.validators.ValidateLimitedTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    @Size(min = 5, max = 64, message = "Patient name must be of length 5-64")
    private String name;

    @Column(name = "phone_number", nullable = false)
    @NotNull(message = "Number cannot be null")
    @Pattern(regexp = "^\\d{9}$", message = "Invalid number")
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    @Email(message = "Invalid email", regexp = CommonConstants.EMAIL_REGEX)
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Address cannot be empty")
    @Size(min = 5, message = "Address must be at least 5 characters long")
    private String address;

    @Column(name = "date_of_birth", nullable = false)
    @NotNull(message = "Date of birth cannot be null")
    @ValidateLimitedTime(timeframe = TimeFrame.PAST, message = "Invalid date of birth")
    private LocalDate dateOfBirth;


    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    @NotNull(message = "Gender cannot be null")
    private Gender gender;

    public Patient(String name, String phoneNumber, String email, String address, LocalDate dateOfBirth, Gender gender) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
    }
}
