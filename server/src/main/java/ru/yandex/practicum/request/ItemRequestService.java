package ru.yandex.practicum.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.item.ItemRepository;
import ru.yandex.practicum.item.dto.ItemForRequestDao;
import ru.yandex.practicum.request.dto.ItemRequestDto;
import ru.yandex.practicum.request.exceptions.RequestNotFoundException;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.groupingBy;

@Service
@RequiredArgsConstructor
public class ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestDtoMapper itemRequestDtoMapper;

    @Transactional
    public ItemRequest addNewRequest(Long userId, ItemRequestDto itemRequestDto) {
        ItemRequest newItemRequest = itemRequestDtoMapper.mapToItemRequest(userId, itemRequestDto);
        return itemRequestRepository.save(newItemRequest);
    }

    public List<ItemRequestDto> getUserRequests(Long userId) {
        List<ItemRequest> usersRequests = itemRequestRepository.findItemRequestByUserId(userId);
        List<Long> requestsId = usersRequests.stream().map(ItemRequest::getId).toList();
        List<ItemForRequestDao> itemsForRequests = itemRepository.findByRequestId(requestsId);
        Map<Long, List<ItemForRequestDao>> itemMapForAssignment = itemsForRequests.stream()
                .collect(groupingBy(ItemForRequestDao::getRequestId));
        List<ItemRequestDto> usersRequestsDto = usersRequests.stream().map(itemRequestDtoMapper::mapToItemRequestDto)
                .sorted(Comparator.comparing(ItemRequestDto::getCreated))
                .toList();
        for (ItemRequestDto itemRequestDto : usersRequestsDto) {
            if (itemMapForAssignment.containsKey(itemRequestDto.getId())) {
                itemRequestDto.setItems(itemMapForAssignment.get(itemRequestDto.getId()));
            }
        }
        return usersRequestsDto;
    }

    public List<ItemRequestDto> getAllRequests(Long userId) {
        List<ItemRequest> allAvailableRequests = itemRequestRepository.findAll();
        return allAvailableRequests.stream()
                .filter(itemRequest -> !Objects.equals(itemRequest.getUserId(), userId))
                .map(itemRequestDtoMapper::mapToItemRequestDto)
                .sorted(Comparator.comparing(ItemRequestDto::getCreated))
                .toList();
    }

    public ItemRequestDto getItemRequest(Long userId, Long requestId) {
        Optional<ItemRequest> requiredRequest = itemRequestRepository.findItemRequestById(requestId);
        if (requiredRequest.isEmpty()) {
            throw new RequestNotFoundException("Запрос с ID " + requestId + " отсутствует");
        }
        ItemRequestDto requiredRequestDto = itemRequestDtoMapper.mapToItemRequestDto(requiredRequest.get());
        requiredRequestDto.setItems(itemRepository.findByRequestId(requestId));
        return requiredRequestDto;
    }
}
