package ru.yandex.practicum.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class ItemRequestDto {
    @NotBlank(message = "Требуется указать описание запроса")
    private String description;
}
