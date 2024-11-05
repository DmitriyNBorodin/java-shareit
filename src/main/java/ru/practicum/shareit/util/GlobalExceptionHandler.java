package ru.practicum.shareit.util;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.exceptions.UserValidationException;
import ru.practicum.shareit.util.ErrorMessage;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleUserNotFoundException(UserNotFoundException e) {
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
        return new ErrorMessage(e.getBindingResult().getFieldError().getDefaultMessage());
    }
}
