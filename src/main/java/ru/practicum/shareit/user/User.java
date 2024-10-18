package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    Long id;
    @NotNull(message = "Поле name не должно быть пустым")
    String name;
    @Email(message = "Поле email должно соответствовать шаблону электронной почты")
    @NotNull(message = "Поле email не должно быть пустым")
    String email;
}
