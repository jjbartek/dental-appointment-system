package com.das.common.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnumListValidator implements ConstraintValidator<ValidateEnumValue, List<String>> {
    private List<String> enumValues;

    @Override
    public void initialize(ValidateEnumValue annotation) {
        enumValues = Arrays.stream(annotation.value().getEnumConstants())
                .map(Enum::toString)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(List<String> list, ConstraintValidatorContext context) {
        if (list == null) return true;
        for (String value : list) {
            if (!enumValues.contains(value)) return false;
        }

        return true;
    }
}