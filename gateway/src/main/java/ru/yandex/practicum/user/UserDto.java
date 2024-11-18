package ru.yandex.practicum.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.util.Marker;

@Data
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    Long id;
    @NotNull(message = "Поле name не должно быть указано", groups = {Marker.OnCreate.class})
    String name;
    @Email(message = "Поле email должно соответствовать шаблону электронной почты",
            groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    @NotNull(message = "Поле email не должно быть пустым", groups = {Marker.OnCreate.class})
    String email;
}