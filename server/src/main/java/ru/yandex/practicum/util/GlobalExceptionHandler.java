package ru.yandex.practicum.util;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.item.exceptions.ItemNotFoundException;
import ru.yandex.practicum.user.exceptions.UserNotFoundException;
import ru.yandex.practicum.user.exceptions.UserValidationException;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({UserNotFoundException.class, ItemNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleUserNotFoundException(RuntimeException e) {
        log.info("NotFoundException {}", e.getMessage());
        return e.getMessage();
    }

    @ExceptionHandler(UserValidationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessage handleUserValidationException(UserValidationException e) {
        log.info("UserValidationException {}", e.getMessage());
        return new ErrorMessage(e.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleValidationException(ValidationException e) {
        log.info("ValidationException {}", e.getMessage());
        return new ErrorMessage(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleArgumentException(MethodArgumentNotValidException e) {
        log.info("Not valid argument  - {}", e.getMessage());
        return new ErrorMessage(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }
}
