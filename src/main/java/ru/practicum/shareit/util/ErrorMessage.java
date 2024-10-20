package ru.practicum.shareit.util;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class ErrorMessage {
    private final String message;
}
