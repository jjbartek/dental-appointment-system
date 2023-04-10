package com.das.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.passay.*;

import java.util.Arrays;

public class PasswordValidator implements ConstraintValidator<ValidatePassword, String> {

    @Override
    public void initialize(final ValidatePassword annotation) {}

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null) return true;

        org.passay.PasswordValidator validator = new org.passay.PasswordValidator(Arrays.asList(
                new LengthRule(8, 30),
                new WhitespaceRule(),
                new CharacterRule(PolishCharacterData.UpperCase, 1),
                new CharacterRule(PolishCharacterData.LowerCase, 1),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new CharacterRule(EnglishCharacterData.Special, 1)));

        RuleResult result = validator.validate(new PasswordData(value));
        if (result.isValid()) {
            return true;
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
                String.join("\n", validator.getMessages(result))
        ).addConstraintViolation();
        return false;
    }
}
