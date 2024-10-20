package ru.practicum.shareit.item;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemDtoMapper itemDtoMapper;
    private final UserRepository userRepository;

    public ItemDto addNewItem(Long ownerId, ItemDto newItemDto) {
        userRepository.validateUser(ownerId);
        Item newItem = itemDtoMapper.convertDtoToItem(ownerId, newItemDto);
        newItem = itemRepository.addItem(newItem);
        return itemDtoMapper.convertItemToDto(newItem);
    }

    public ItemDto updateItem(Long userId, ItemDto itemToUpdate, Long itemId) {
        userRepository.validateUser(userId);
        Item updatingItem = itemRepository.getItemById(itemId);
        if (!updatingItem.getOwnerId().equals(userId)) {
            throw new ValidationException("Редактировать предмет может только владелец");
        }
        if (itemToUpdate.getName() != null) {
            updatingItem.setName(itemToUpdate.getName());
        }
        if (itemToUpdate.getDescription() != null) {
            updatingItem.setDescription(itemToUpdate.getDescription());
        }
        if (itemToUpdate.getAvailable() != null) {
            updatingItem.setAvailable(itemToUpdate.getAvailable());
        }
        itemRepository.addItem(updatingItem);
        return itemDtoMapper.convertItemToDto(updatingItem);
    }

    public ItemDto getItemById(Long itemId) {
        log.info("Получение предмета по id {}", itemId);
        Item requestedItem = itemRepository.getItemById(itemId);
        return itemDtoMapper.convertItemToDto(requestedItem);
    }

    public List<ItemDto> getItemsByOwnerId(Long ownerId) {
        log.info("Получение предметов пользователя с id {}", ownerId);
        userRepository.validateUser(ownerId);
        List<ItemDto> allItemsByUserId = itemRepository.getItemsByOwnerId(ownerId).stream().map(itemDtoMapper::convertItemToDto).toList();
        log.info("Получено предметов : {}", allItemsByUserId.size());
        return allItemsByUserId;
    }

    public List<ItemDto> searchForItems(String text) {
        log.info("Поиск предметов по строке {}", text);
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.searchForItems(text).stream().map(itemDtoMapper::convertItemToDto).toList();
    }
}
