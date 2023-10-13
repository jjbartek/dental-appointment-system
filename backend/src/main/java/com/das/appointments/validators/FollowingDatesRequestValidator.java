package com.das.appointments.validators;

import com.das.appointments.AppointmentRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FollowingDatesRequestValidator implements ConstraintValidator<ValidateFollowingDates, AppointmentRequest> {
    @Override
    public void initialize(final ValidateFollowingDates annotation) {
    }

    @Override
    public boolean isValid(AppointmentRequest appointment, ConstraintValidatorContext context) {
       return appointment.getStartTime().isBefore(appointment.getEndTime());
    }
}
