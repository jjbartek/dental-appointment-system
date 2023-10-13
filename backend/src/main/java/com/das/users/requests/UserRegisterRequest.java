package com.das.users.requests;

import com.das.common.constants.CommonConstants;
import com.das.users.validators.ValidatePassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest {
    @NotBlank(message = "Username cannot be empty")
    @Size(min = 5, max = 64, message = "User name must be of length 5-64")
    private String username;
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email", regexp = CommonConstants.EMAIL_REGEX)
    private String email;

    @NotNull(message = "Password cannot be null")
    @ValidatePassword
    private String password;
}
