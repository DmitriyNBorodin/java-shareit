package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder(toBuilder = true)
public class ItemDto {
    Long id;
    @NotBlank(message = "Поле name не должно быть пустым")
    String name;
    @NotBlank(message = "Поле description не должно быть пустым")
    String description;
    @NotNull(message = "Требуется поле available")
    Boolean available;
}
