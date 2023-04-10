package com.das.requests;

import com.das.config.AppConstants;
import com.das.validators.ValidateLimitedTime;
import com.das.validators.ValidateTimeFrame;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientRequest {

    @NotBlank(message = "Patient name cannot be empty")
    @Size(min = 5, max = 64, message = "Patient name must be of length 5-64")
    private String name;

    @NotNull(message = "Number cannot be null")
    @Pattern(regexp = "^\\d{9}$", message = "Invalid number")
    private String phoneNumber;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Invalid email", regexp = AppConstants.EMAIL_REGEX)
    private String email;

    @NotBlank(message = "Address cannot be empty")
    @Size(min = 5, message = "Address must be at least 5 characters long")
    private String address;

    @NotNull(message = "Date of birth cannot be null")
    @ValidateLimitedTime(timeframe = ValidateTimeFrame.PAST, message = "Invalid date of birth")
    private LocalDate dateOfBirth;
}
