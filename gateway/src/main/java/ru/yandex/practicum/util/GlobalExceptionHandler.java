package ru.yandex.practicum.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleArgumentException(MethodArgumentNotValidException e) {
        log.info("Not valid argument  - {}", e.getMessage());
        return new ErrorMessage(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }
}
