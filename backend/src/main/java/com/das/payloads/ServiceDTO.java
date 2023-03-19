package com.das.payloads;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDTO {
    @NotBlank(message = "Service name cannot be empty")
    @NotNull(message = "Service name cannot be null")
    private String name;


    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 4, fraction = 2)
    @NotNull(message = "Minimal price cannot be null")
    private BigDecimal minPrice;

    @Min(value = 1, message = "Service duration must be greater than 0")
    @NotNull(message = "Duration cannot be null")
    private Integer duration;
}
