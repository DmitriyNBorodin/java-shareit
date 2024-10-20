package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    Long id;
    @NotNull(message = "Поле name не должно быть пустым")
    String name;
    @Email(message = "Поле email должно соответствовать шаблону электронной почты")
    @NotNull(message = "Поле email не должно быть пустым")
    String email;
}
