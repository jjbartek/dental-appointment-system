package com.das.requests;

import com.das.entities.Role;
import com.das.validators.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {
    @NotBlank(message = "Username cannot be empty")
    private String name;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Invalid email")
    private String email;

    @NotNull(message = "Password cannot be null")
    @ValidPassword
    private String password;

    @NotEmpty(message = "Role list cannot be empty")
    private List<Role> roles;

}
