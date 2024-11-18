package ru.yandex.practicum.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
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
    private ItemRequestClient itemRequestClient;

    @Test
    void addNewRequestTest() throws Exception {
        ItemRequestDto itemRequest = new ItemRequestDto("anything");

        when(itemRequestClient.addNewRequest(Mockito.anyLong(), Mockito.any(ItemRequestDto.class)))
                .thenReturn(new ResponseEntity<>(itemRequest, HttpStatus.OK));

        mvc.perform(post("/requests").content(mapper.writeValueAsString(itemRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 3L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getUserRequestsTest() throws Exception {
        ItemRequestDto itemDto1 = new ItemRequestDto("123");
        ItemRequestDto itemDto2 = new ItemRequestDto("321");

        when(itemRequestClient.getUserRequests(4L)).thenReturn(new ResponseEntity<>(List.of(itemDto1, itemDto2), HttpStatus.OK));

        mvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 4L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllRequestsTest() throws Exception {
        ItemRequestDto itemDto1 = new ItemRequestDto("123");
        ItemRequestDto itemDto2 = new ItemRequestDto("321");

        when(itemRequestClient.getAllRequests(5L)).thenReturn(new ResponseEntity<>(List.of(itemDto1, itemDto2), HttpStatus.OK));

        mvc.perform(get("/requests/all")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 5L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getItemRequestTest() throws Exception {
        ItemRequestDto itemDto = new ItemRequestDto("anything");

        when(itemRequestClient.getItemRequest(3L, 1L))
                .thenReturn(new ResponseEntity<>(itemDto, HttpStatus.OK));

        mvc.perform(get("/requests/{requestId}", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 3L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("anything"), ItemRequestDto.class));
    }
}