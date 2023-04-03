package com.das.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class PastDateImpl implements ConstraintValidator<PastDate, LocalDate> {
    @Override
    public void initialize(final PastDate annotation) {}

    @Override
    public boolean isValid(LocalDate passedDate, ConstraintValidatorContext context) {
        return passedDate.isBefore(LocalDate.now());
    }
}