package com.das.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "services")
@NoArgsConstructor
@AllArgsConstructor
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "service_name", nullable = false)
    @NotBlank(message = "Service name cannot be empty")
    @NotNull(message = "Service name cannot be null")
    private String name;

    @Column(name = "min_price", nullable = false, columnDefinition = "DECIMAL(6, 2)")
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 4, fraction = 2)
    @NotNull(message = "Minimal price cannot be null")
    private BigDecimal minPrice;

    @Column(nullable = false)
    @Min(value = 1, message = "Service duration must be greater than 0")
    @NotNull(message = "Duration cannot be null")
    private Integer duration;
}
