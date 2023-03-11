package com.das.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PastDateImpl.class)
public @interface PastDate {
    String message() default "{com.das.PastDate.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
