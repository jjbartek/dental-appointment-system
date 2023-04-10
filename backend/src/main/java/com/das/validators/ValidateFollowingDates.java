package com.das.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;

@Constraint(validatedBy = {FollowingDatesAppointmentValidator.class, FollowingDatesRequestValidator.class})
@Target({TYPE,ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateFollowingDates {
    String message() default "The first date must be before the second";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
