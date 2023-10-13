package com.das.services;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRequest {
    @NotBlank(message = "Service name cannot be empty")
    @Size(min = 5, message = "Service name must be at least 5 characters")
    private String name;

    @Digits(integer = 6, fraction = 2)
    @NotNull(message = "Minimal price cannot be null")
    private BigDecimal minPrice;

    @Min(value = 1, message = "Service duration must be greater than 0")
    @NotNull(message = "Duration cannot be null")
    private Integer duration;
}
