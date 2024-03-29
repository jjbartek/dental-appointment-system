package com.das.appointments;

import com.das.appointments.entities.AppointmentStatus;
import com.das.common.entities.TimeFrame;
import com.das.common.validators.ValidateEnumValue;
import com.das.appointments.validators.ValidateFollowingDates;
import com.das.common.validators.ValidateLimitedTime;
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
@ValidateFollowingDates(message = "Start-time has to be before end-time")
public class AppointmentRequest {

    @NotNull(message = "Employee's id cannot be null")
    private Integer employeeId;

    @NotNull(message = "Patient's id cannot be null")
    private Integer patientId;

    @NotNull(message = "Start time cannot be null")
    @ValidateLimitedTime(timeframe = TimeFrame.FUTURE, message = "Start of the visit have to be in the future")
    private LocalDateTime startTime;

    @NotNull(message = "End time cannot be null")
    @ValidateLimitedTime(timeframe = TimeFrame.FUTURE, message = "End of the visit have to be in the future")
    private LocalDateTime endTime;

    private String notes;

    @NotNull(message = "Status cannot be null")
    @ValidateEnumValue(value = AppointmentStatus.class, message = "Invalid value for status")
    private String status;

    @NotEmpty(message = "List of services must have at least one service")
    private List<Integer> serviceIds;

    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 4, fraction = 2)
    private BigDecimal total;

}
