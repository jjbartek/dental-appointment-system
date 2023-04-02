package com.das.payloads;

import com.das.entities.Status;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRequest {

    private Integer id;

    @NotNull(message = "Employee's id cannot be null")
    private Integer employeeId;

    @NotNull(message = "Patient;s id cannot be null")
    private Integer patientId;

    @NotNull(message = "Start time cannot be null")
    private LocalDateTime startTime;

    @NotNull(message = "End time cannot be null")
    private LocalDateTime endTime;

    private String notes;

    @NotNull(message = "Status cannot be null")
    private Status status;

    @NotNull(message = "List of services cannot be null")
    @NotEmpty(message = "List of services must have at least one service")
    private List<Integer> serviceIds;

    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 4, fraction = 2)
    private BigDecimal total;

}
