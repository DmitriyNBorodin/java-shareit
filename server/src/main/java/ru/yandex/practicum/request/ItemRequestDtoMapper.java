package ru.yandex.practicum.request;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.request.dto.ItemRequestDto;

import java.time.LocalDateTime;

@Component
public class ItemRequestDtoMapper {
    public ItemRequest mapToItemRequest(Long userId, ItemRequestDto itemRequestDto) {
        ItemRequest newItemRequest = ItemRequest.builder()
                .description(itemRequestDto.getDescription())
                .created(LocalDateTime.now())
                .userId(userId)
                .build();
        if (itemRequestDto.getId() != null) {
            newItemRequest.setId(itemRequestDto.getId());
        }
        return newItemRequest;
    }

    public ItemRequestDto mapToItemRequestDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .userId(itemRequest.getUserId())
                .created(itemRequest.getCreated())
                .build();
    }
}
