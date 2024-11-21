package ru.yandex.practicum.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private ItemClient itemClient;

    @Test
    void addNewItem() throws Exception {
        ItemDto itemDto = ItemDto.builder().name("name")
                .description("description").available(true).build();
        when(itemClient.addNewItem(1L, itemDto)).thenAnswer(invocationOnMock -> {
            ItemDto savedItemDto = invocationOnMock.getArgument(1, ItemDto.class);
            savedItemDto.setId(1L);
            return new ResponseEntity<>(savedItemDto, HttpStatus.OK);
        });

        mvc.perform(post("/items").content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class));
    }

    @Test
    void addNewComment() throws Exception {
        CommentDto commentDto = CommentDto.builder()
                .text("123").build();
        when(itemClient.addNewItemComment(3L, 1L, commentDto)).thenAnswer(
                invocationOnMock -> {
                    CommentDto savedComment = invocationOnMock.getArgument(2, CommentDto.class);
                    savedComment.setId(4L);
                    return new ResponseEntity<>(savedComment, HttpStatus.OK);
                }
        );

        mvc.perform(post("/items/{itemId}/comment", 1L).content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 3L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(4L), Long.class));
    }

    @Test
    void updateItem() throws Exception {
        ItemDto itemDto = ItemDto.builder().id(4L).name("name")
                .description("description").available(true).build();
        when(itemClient.updateItem(1L, itemDto, 4L)).thenAnswer(invocationOnMock -> {
            ItemDto updatingItem = invocationOnMock.getArgument(1, ItemDto.class);
            updatingItem.setAvailable(false);
            return new ResponseEntity<>(updatingItem, HttpStatus.OK);
        });

        mvc.perform(patch("/items/{itemId}", 4L)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available", is("false"), String.class));
    }

    @Test
    void getItemById() throws Exception {
        ItemDto itemDto = ItemDto.builder().id(4L).name("name")
                .description("description").available(true).build();
        when(itemClient.getItemById(4L)).thenReturn(new ResponseEntity<>(itemDto, HttpStatus.OK));

        mvc.perform(get("/items/{itemId}", 4L)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("name"), String.class));
    }

    @Test
    void getItemsByOwnerId() throws Exception {
        ItemDto itemDto = ItemDto.builder().id(4L).name("name")
                .description("description").available(true).build();
        when(itemClient.getItemsByOwnerId(3L)).thenReturn(new ResponseEntity<>(List.of(itemDto), HttpStatus.OK));

        mvc.perform(get("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 3L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void searchForItem() throws Exception {
        ItemDto itemDto = ItemDto.builder().id(4L).name("name")
                .description("description").available(true).build();
        when(itemClient.searchForItems("123")).thenReturn(new ResponseEntity<>(List.of(itemDto), HttpStatus.OK));

        mvc.perform(get("/items/search")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 3L)
                        .param("text", "123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}