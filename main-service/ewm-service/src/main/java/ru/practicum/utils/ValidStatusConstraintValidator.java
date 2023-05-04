package ru.practicum.utils;

import ru.practicum.enums.Status;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidStatusConstraintValidator implements ConstraintValidator<ValidStatus, Status> {

    @Override
    public void initialize(ValidStatus constraintAnnotation) {
    }

    @Override
    public boolean isValid(Status value, ConstraintValidatorContext context) {
        return value == Status.CONFIRMED || value == Status.REJECTED;
    }
}