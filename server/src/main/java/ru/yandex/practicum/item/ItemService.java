package ru.yandex.practicum.item;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.booking.Booking;
import ru.yandex.practicum.booking.BookingRepository;
import ru.yandex.practicum.booking.BookingStatus;
import ru.yandex.practicum.item.dto.CommentDto;
import ru.yandex.practicum.item.dto.ItemDto;
import ru.yandex.practicum.item.model.Comment;
import ru.yandex.practicum.item.model.Item;
import ru.yandex.practicum.user.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemDtoMapper itemDtoMapper;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentsRepository commentRepository;
    private final CommentDtoMapper commentDtoMapper;

    @Transactional
    public ItemDto addNewItem(Long ownerId, ItemDto newItemDto) {
        userService.validateUser(ownerId);
        Item newItem = itemDtoMapper.convertDtoToItem(ownerId, newItemDto);
        newItem = itemRepository.save(newItem);
        return itemDtoMapper.convertItemToDto(newItem);
    }

    @Transactional
    public CommentDto addNewItemComment(Long userId, Long itemId, CommentDto commentDto) {
        Optional<Booking> bookingForComment = bookingRepository.findBookingByItemIdAndBookerId(itemId, userId);
        validateAbilityToComment(bookingForComment);
        Comment newComment = commentDtoMapper.toComment(commentDto, bookingForComment.get());
        return commentDtoMapper.toCommentDto(commentRepository.save(newComment));
    }

    @Transactional
    public ItemDto updateItem(Long userId, ItemDto itemToUpdate, Long itemId) {
        userService.validateUser(userId);
        Item updatingItem = itemRepository.getReferenceById(itemId);
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
        itemRepository.save(updatingItem);
        return itemDtoMapper.convertItemToDto(updatingItem);
    }

    public ItemDto getItemById(Long itemId) {
        log.info("Получение предмета по id {}", itemId);
        Item requestedItem = itemRepository.getReferenceById(itemId);
        ItemDto requestedItemDto = itemDtoMapper.convertItemToDto(requestedItem);
        List<Booking> itemBookings = bookingRepository.findBookingByItemId(itemId);
        log.info("Получение списка бронирований, размер списка {}", itemBookings.size());
        log.info("{}", itemBookings);
        requestedItemDto.setLastBooking(itemBookings.stream().map(Booking::getEnd)
                .filter(bookingEnd -> bookingEnd.isAfter(LocalDateTime.now())).max(LocalDateTime::compareTo).orElse(null));
        requestedItemDto.setNextBooking(itemBookings.stream().map(Booking::getStart)
                .filter(bookingStart -> bookingStart.isAfter(LocalDateTime.now())).min(LocalDateTime::compareTo).orElse(null));
        List<CommentDto> requestedItemComments = commentRepository.getCommentsByItemId(itemId)
                .stream().map(commentDtoMapper::toCommentDto).toList();
        log.info("Комментарии {}", requestedItemComments);
        if (requestedItemComments.isEmpty()) {
            requestedItemDto.setComments(new ArrayList<>());
        } else {
            requestedItemDto.setComments(requestedItemComments);
        }
        return requestedItemDto;
    }

    public List<ItemDto> getItemsByOwnerId(Long ownerId) {
        log.info("Получение предметов пользователя с id {}", ownerId);
        userService.validateUser(ownerId);
        List<ItemDto> allItemsByUserId = itemRepository.findItemByOwnerId(ownerId).stream().map(itemDtoMapper::convertItemToDto).toList();
        log.info("Получено предметов : {}", allItemsByUserId.size());
        return allItemsByUserId;
    }

    public List<ItemDto> searchForItems(String text) {
        log.info("Поиск предметов по строке {}", text);
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        List<Item> requiredItems = itemRepository.getByNameContainingOrDescriptionContainingAllIgnoreCase(text, text);
        return requiredItems.stream().filter(Item::getAvailable).map(itemDtoMapper::convertItemToDto).toList();
    }

    private void validateAbilityToComment(Optional<Booking> bookingForComment) {
        if (bookingForComment.isPresent()) {
            Booking booking = bookingForComment.get();
            if (booking.getStatus().equals(BookingStatus.APPROVED) && booking.getEnd().isBefore(LocalDateTime.now())) {
                return;
            }
        }
        throw new ValidationException("Оставлять отзыв можно только после аренды предмета");
    }
}
