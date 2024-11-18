package ru.yandex.practicum.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.item.ItemRepository;
import ru.yandex.practicum.request.dto.ItemRequestDto;
import ru.yandex.practicum.request.exceptions.RequestNotFoundException;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;

    public ItemRequest addNewRequest(Long userId, ItemRequestDto itemRequestDto) {
        ItemRequest newItemRequest = mapToItemRequest(userId, itemRequestDto);
        return itemRequestRepository.save(newItemRequest);
    }

    public List<ItemRequestDto> getUserRequests(Long userId) {
        List<ItemRequest> usersRequests = itemRequestRepository.findItemRequestByUserId(userId);
        List<ItemRequestDto> usersRequestsDto = usersRequests.stream().map(this::mapToItemRequestDto)
                .sorted(Comparator.comparing(ItemRequestDto::getCreated))
                .toList();
        for (ItemRequestDto itemRequestDto : usersRequestsDto) {
            itemRequestDto.setItems(itemRepository.findByRequestId(itemRequestDto.getId()));
        }
        return usersRequestsDto;
    }

    public List<ItemRequestDto> getAllRequests(Long userId) {
        List<ItemRequest> allAvailableRequests = itemRequestRepository.findAll();
        List<ItemRequestDto> allAvailableRequestsDto = allAvailableRequests.stream()
                .filter(itemRequest -> !Objects.equals(itemRequest.getUserId(), userId))
                .map(this::mapToItemRequestDto)
                .sorted(Comparator.comparing(ItemRequestDto::getCreated))
                .toList();
        return allAvailableRequestsDto;
    }

    public ItemRequestDto getItemRequest(Long userId, Long requestId) {
        Optional<ItemRequest> requiredRequest = itemRequestRepository.findItemRequestById(requestId);
        if (requiredRequest.isEmpty()) {
            throw new RequestNotFoundException("Запрос с ID " + requestId + " отсутствует");
        }
        ItemRequestDto requiredRequestDto = mapToItemRequestDto(requiredRequest.get());
        requiredRequestDto.setItems(itemRepository.findByRequestId(requestId));
        return requiredRequestDto;
    }

    private ItemRequest mapToItemRequest(Long userId, ItemRequestDto itemRequestDto) {
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

    private ItemRequestDto mapToItemRequestDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .userId(itemRequest.getUserId())
                .created(itemRequest.getCreated())
                .build();
    }
}
