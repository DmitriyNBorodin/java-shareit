package ru.yandex.practicum.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.item.ItemRepository;
import ru.yandex.practicum.request.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceTest {
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private ItemRequestService itemRequestService;
    @Captor
    private ArgumentCaptor<ItemRequest> itemRequestArgumentCaptor;

    @Test
    void addNewRequest() {
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "anything", 1L,
                LocalDateTime.now(), null);
        ItemRequest itemRequest = new ItemRequest(1L, "anything", 1L, LocalDateTime.now());
        when(itemRequestRepository.save(Mockito.any(ItemRequest.class))).thenReturn(itemRequest);

        itemRequestService.addNewRequest(1L, itemRequestDto);

        verify(itemRequestRepository).save(itemRequestArgumentCaptor.capture());
        ItemRequest savedRequest = itemRequestArgumentCaptor.getValue();

        assertEquals(itemRequest.getDescription(), savedRequest.getDescription());
    }

    @Test
    void getUserRequests() {
        ItemRequest itemRequest = new ItemRequest(1L, "anything",
                1L, LocalDateTime.now());

        when(itemRequestRepository.findItemRequestByUserId(Mockito.anyLong())).thenReturn(List.of(itemRequest));
        when(itemRepository.findByRequestId(Mockito.anyLong())).thenReturn(new ArrayList<>());

        List<ItemRequestDto> listOfRequests = itemRequestService.getUserRequests(1L);

        assertEquals(1, listOfRequests.size());
        verify(itemRequestRepository, atLeast(1)).findItemRequestByUserId(Mockito.anyLong());
        verify(itemRepository, atLeast(1)).findByRequestId(Mockito.anyLong());
    }

    @Test
    void getAllRequests() {
        ItemRequest itemRequest1 = new ItemRequest(1L, "anything",
                1L, LocalDateTime.now());
        ItemRequest itemRequest2 = new ItemRequest(2L, "anythingElse",
                1L, LocalDateTime.now());

        when(itemRequestRepository.findAll()).thenReturn(List.of(itemRequest1, itemRequest2));

        List<ItemRequestDto> allRequests = itemRequestService.getAllRequests(2L);

        assertEquals(2, allRequests.size());
        verify(itemRequestRepository, atLeast(1)).findAll();
    }

    @Test
    void getItemRequest() {
        ItemRequest itemRequest = new ItemRequest(1L, "anything",
                1L, LocalDateTime.now());
        when(itemRequestRepository.findItemRequestById(Mockito.anyLong())).thenReturn(Optional.of(itemRequest));
        when(itemRepository.findByRequestId(Mockito.anyLong())).thenReturn(new ArrayList<>());

        ItemRequestDto itemRequestDto = itemRequestService.getItemRequest(1L, 1L);

        assertEquals(itemRequest.getDescription(), itemRequestDto.getDescription());
        verify(itemRequestRepository, atLeast(1)).findItemRequestById(Mockito.anyLong());
        verify(itemRepository, atLeast(1)).findByRequestId(Mockito.anyLong());
    }
}