package com.das.common.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnumValueValidator implements ConstraintValidator<ValidateEnumValue, String> {
    private List<String> enumValues;

    @Override
    public void initialize(ValidateEnumValue annotation) {
        enumValues = Arrays.stream(annotation.value().getEnumConstants())
                    .map(Enum::toString)
                    .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || (enumValues != null && enumValues.contains(value));
    }
}