package com.das.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.TYPE_USE, ElementType.FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {
        EnumValueValidator.class,
        EnumListValidator.class
})
public @interface ValidateEnumValue {
    String message() default "Invalid parameter";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<? extends Enum<?>> value();
}
