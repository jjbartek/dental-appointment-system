package com.das.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class LimitedTimeLocalDateValidator implements ConstraintValidator<ValidateLimitedTime, LocalDate> {
    private ValidateTimeFrame timeframe;

    @Override
    public void initialize(final ValidateLimitedTime annotation) {
        timeframe = annotation.timeframe();
    }

    @Override
    public boolean isValid(LocalDate passedDate, ConstraintValidatorContext context) {
        if (timeframe == ValidateTimeFrame.FUTURE) {
            return passedDate.isAfter(LocalDate.now());
        } else {
            return passedDate.isBefore(LocalDate.now());
        }
    }
}