package com.das.payloads;

import com.das.entities.Role;
import com.das.validators.EnumValidator;
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
public class UserUpdateDTO {
    @NotBlank(message = "Username cannot be empty")
    @NotNull(message = "Username cannot be null")
    private String name;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Invalid email")
    private String email;

    private String password;

    @NotNull(message = "Role list cannot be null")
    @NotEmpty(message = "Role list cannot be empty")
    private List<@NotNull @EnumValidator(targetClassType = Role.class, message = "Invalid role") Role> roles;

}
