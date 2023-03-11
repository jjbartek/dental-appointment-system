package com.das.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.sql.Date;

public class PastDateImpl implements ConstraintValidator<PastDate, Date> {
    @Override
    public void initialize(final PastDate annotation) {}

    @Override
    public boolean isValid(Date passedDate, ConstraintValidatorContext context) {
        return passedDate.before(new Date(System.currentTimeMillis()));
    }
}