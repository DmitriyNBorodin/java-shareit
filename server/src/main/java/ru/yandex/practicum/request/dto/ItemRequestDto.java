package ru.yandex.practicum.request.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.item.dto.ItemForRequestDao;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class ItemRequestDto {
    private Long id;
    private String description;
    private Long userId;
    private LocalDateTime created;
    private List<ItemForRequestDao> items;
}
