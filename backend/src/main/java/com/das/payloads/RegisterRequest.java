package com.das.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Username cannot be empty")
    @NotNull(message = "Username cannot be null")
    private String username;
    @NotBlank(message = "Email cannot be empty")
    @NotNull(message = "Email cannot be null")
    private String email;
    @NotBlank(message = "Password cannot be empty")
    @NotNull(message = "Password cannot be null")
    private String password;
}
