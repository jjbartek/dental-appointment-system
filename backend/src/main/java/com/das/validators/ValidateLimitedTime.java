package com.das.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {LimitedTimeLocalDateValidator.class, LimitedTimeLocalDateTimeValidator.class})
public @interface ValidateLimitedTime {
    String message() default "Time is not in the correct time frame (past/future)";
    TimeFrame timeframe() default TimeFrame.FUTURE;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
