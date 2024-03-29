package com.das.appointments.validators;

import com.das.appointments.entities.Appointment;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FollowingDatesAppointmentValidator implements ConstraintValidator<ValidateFollowingDates, Appointment> {
    @Override
    public void initialize(final ValidateFollowingDates annotation) {
    }

    @Override
    public boolean isValid(Appointment appointment, ConstraintValidatorContext context) {
       return appointment.getStartTime().isBefore(appointment.getEndTime());
    }
}
