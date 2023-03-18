package com.das.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

public class EnumValidatorImpl implements ConstraintValidator<EnumValidator, Object> {
    private Set<String> allowedValues;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void initialize(EnumValidator targetEnum) {
        Class<? extends Enum> enumSelected = targetEnum.targetClassType();
        allowedValues = (Set<String>) EnumSet.allOf(enumSelected).stream().map(e -> ((Enum<? extends Enum<?>>) e).name())
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        return value == null || allowedValues.contains(value.toString());
    }
}