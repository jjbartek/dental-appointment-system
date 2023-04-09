package com.das.requests;

import com.das.config.AppConstants;
import com.das.entities.Role;
import com.das.validators.ValidPassword;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    @NotBlank(message = "Username cannot be empty")
    @Size(min = 5, max = 64, message = "User name must be of length 5-64")
    private String name;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email", regexp = AppConstants.EMAIL_REGEX)
    private String email;

    @ValidPassword
    private String password;

    @NotEmpty(message = "Role list cannot be empty")
    private List<Role> roles;

}
