package com.das.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class LimitedTimeLocalDateTimeValidator implements ConstraintValidator<ValidateLimitedTime, LocalDateTime> {
    private ValidateTimeFrame timeframe;

    @Override
    public void initialize(final ValidateLimitedTime annotation) {
        timeframe = annotation.timeframe();
    }

    @Override
    public boolean isValid(LocalDateTime passedDate, ConstraintValidatorContext context) {
        if (timeframe == ValidateTimeFrame.FUTURE) {
            return passedDate.isAfter(LocalDateTime.now());
        } else {
            return passedDate.isBefore(LocalDateTime.now());
        }
    }
}