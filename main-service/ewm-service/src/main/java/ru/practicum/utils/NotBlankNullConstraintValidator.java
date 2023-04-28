package ru.practicum.utils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotBlankNullConstraintValidator implements ConstraintValidator<NotBlankNull, String> {

    @Override
    public boolean isValid(String text, ConstraintValidatorContext constraintValidatorContext) {
        if (text == null) {
            return true;
        } else {
            return !text.isBlank();
        }
    }
}