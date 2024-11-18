package ru.yandex.practicum.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.item.dto.ItemDto;
import ru.yandex.practicum.item.model.Item;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemDtoMapperTest {
    @Autowired
    private ItemDtoMapper itemDtoMapper;

    @Test
    void convertItemToDto() {
        Item testItem = new Item(1L, 1L, "name", "description", true, null);
        ItemDto testItemDto = itemDtoMapper.convertItemToDto(testItem);

        assertEquals(testItem.getName(), testItemDto.getName());
    }

    @Test
    void convertDtoToItem() {
        ItemDto testItemDto = new ItemDto(1L, "name", "description", true, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(3), null, null);
        Item testItem = itemDtoMapper.convertDtoToItem(1L, testItemDto);

        assertEquals(testItemDto.getName(), testItem.getName());
        assertEquals(1L, testItem.getOwnerId());
    }
}