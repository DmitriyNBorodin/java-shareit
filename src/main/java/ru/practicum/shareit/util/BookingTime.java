package ru.practicum.shareit.util;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = BookingTimeValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BookingTime {
    String message() default "Некорректное время бронирования";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
