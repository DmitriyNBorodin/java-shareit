package ru.yandex.practicum.item;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.item.dto.ItemDto;
import ru.yandex.practicum.item.model.Item;

@Component
public class ItemDtoMapper {
    public ItemDto convertItemToDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequestId())
                .build();
    }

    public Item convertDtoToItem(Long ownerId, ItemDto itemDto) {
        return Item.builder()
                .ownerId(ownerId)
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .requestId(itemDto.getRequestId())
                .build();
    }
}
