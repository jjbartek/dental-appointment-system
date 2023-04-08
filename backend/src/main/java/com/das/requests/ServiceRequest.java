package com.das.requests;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRequest {
    @NotBlank(message = "Service name cannot be empty")
    private String name;

    @Digits(integer = 4, fraction = 2)
    @NotNull(message = "Minimal price cannot be null")
    private BigDecimal minPrice;

    @Min(value = 1, message = "Service duration must be greater than 0")
    @NotNull(message = "Duration cannot be null")
    private Integer duration;
}
