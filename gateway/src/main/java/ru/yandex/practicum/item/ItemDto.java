package ru.yandex.practicum.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.util.Marker;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    Long id;
    @NotBlank(message = "Поле name не должно быть пустым", groups = {Marker.OnCreate.class})
    String name;
    @NotBlank(message = "Поле description не должно быть пустым", groups = {Marker.OnCreate.class})
    String description;
    @NotNull(message = "Требуется поле available", groups = {Marker.OnCreate.class})
    Boolean available;
    LocalDateTime lastBooking;
    LocalDateTime nextBooking;
    List<CommentDto> comments;
    Long requestId;
}