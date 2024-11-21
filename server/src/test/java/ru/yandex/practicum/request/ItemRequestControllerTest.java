package ru.yandex.practicum.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.request.dto.ItemRequestDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private ItemRequestService itemRequestService;

    @Test
    void addNewRequestTest() throws Exception {
        ItemRequest itemRequest = ItemRequest.builder()
                .id(1L).created(LocalDateTime.now()).description("anything")
                .build();
        when(itemRequestService.addNewRequest(Mockito.anyLong(), Mockito.any(ItemRequestDto.class))).thenReturn(itemRequest);

        mvc.perform(post("/requests").content(mapper.writeValueAsString(new ItemRequestDto()))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 3L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("anything"), String.class));
    }

    @Test
    void getUserRequestsTest() throws Exception {
        ItemRequestDto itemDto1 = ItemRequestDto.builder().id(1L).description("123").build();
        ItemRequestDto itemDto2 = ItemRequestDto.builder().id(2L).description("321").build();

        when(itemRequestService.getUserRequests(4L)).thenReturn(List.of(itemDto1, itemDto2));

        mvc.perform(get("/requests")
                .characterEncoding(StandardCharsets.UTF_8)
                .header("X-Sharer-User-Id", 4L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllRequestsTest() throws Exception {
        ItemRequestDto itemDto1 = ItemRequestDto.builder().id(1L).description("123").build();
        ItemRequestDto itemDto2 = ItemRequestDto.builder().id(2L).description("321").build();

        when(itemRequestService.getAllRequests(5L)).thenReturn(List.of(itemDto1, itemDto2));

        mvc.perform(get("/requests/all")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 5L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getItemRequestTest() throws Exception {
        ItemRequestDto itemDto = ItemRequestDto.builder().id(1L).description("anything").build();

        when(itemRequestService.getItemRequest(3L, 1L)).thenReturn(itemDto);

        mvc.perform(get("/requests/{requestId}", 1L)
                .characterEncoding(StandardCharsets.UTF_8)
                .header("X-Sharer-User-Id", 3L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("anything"), ItemRequestDto.class));
    }
}