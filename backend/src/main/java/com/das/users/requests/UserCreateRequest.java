package com.das.users.requests;

import com.das.common.constants.CommonConstants;
import com.das.users.entities.UserRole;
import com.das.common.validators.ValidateEnumValue;
import com.das.users.validators.ValidatePassword;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {
    @NotBlank(message = "Username cannot be empty")
    @Size(min = 5, max = 64, message = "User name must be of length 5-64")
    private String name;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email", regexp = CommonConstants.EMAIL_REGEX)
    private String email;

    @NotNull(message = "Password cannot be null")
    @ValidatePassword
    private String password;

    @NotEmpty(message = "Role list cannot be empty")
    @ValidateEnumValue(value = UserRole.class, message = "Invalid value for role")
    private List<String> roles;

}
