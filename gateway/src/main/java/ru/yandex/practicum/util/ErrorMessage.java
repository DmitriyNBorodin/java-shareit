package ru.yandex.practicum.util;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class ErrorMessage {
    private final String error;
}
