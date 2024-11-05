package ru.practicum.shareit.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class BookingTimeValidator implements ConstraintValidator<BookingTime, LocalDateTime> {
    @Override
    public void initialize(BookingTime constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDateTime localDate, ConstraintValidatorContext constraintValidatorContext) {
        return !localDate.isBefore(LocalDateTime.now());
    }
}
