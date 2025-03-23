package ru.yandex.practicum.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.item.dto.CommentDto;
import ru.yandex.practicum.item.dto.ItemDto;

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
    private ItemService itemService;

    @Test
    void addNewItem() throws Exception {
        ItemDto itemDto = ItemDto.builder().name("name")
                .description("description").available(true).build();
        when(itemService.addNewItem(1L, itemDto)).thenAnswer(invocationOnMock -> {
            ItemDto savedItemDto = invocationOnMock.getArgument(1, ItemDto.class);
            savedItemDto.setId(1L);
            return savedItemDto;
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
        when(itemService.addNewItemComment(3L, 1L, commentDto)).thenAnswer(
                invocationOnMock -> {
                    CommentDto savedComment = invocationOnMock.getArgument(2, CommentDto.class);
                    savedComment.setId(4L);
                    return savedComment;
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
        when(itemService.updateItem(1L, itemDto, 4L)).thenAnswer(invocationOnMock -> {
            ItemDto updatingItem = invocationOnMock.getArgument(1, ItemDto.class);
            updatingItem.setAvailable(false);
            return updatingItem;
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
    void updateItemNotByOwnerTest() throws Exception {
        when(itemService.updateItem(Mockito.anyLong(), Mockito.any(ItemDto.class), Mockito.anyLong()))
                .thenThrow(ValidationException.class);
        mvc.perform(patch("/items/{itemId}", 4L)
                        .content(mapper.writeValueAsString(new ItemDto()))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getItemById() throws Exception {
        ItemDto itemDto = ItemDto.builder().id(4L).name("name")
                .description("description").available(true).build();
        when(itemService.getItemById(4L)).thenReturn(itemDto);

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
        when(itemService.getItemsByOwnerId(3L)).thenReturn(List.of(itemDto));

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
        when(itemService.searchForItems("123")).thenReturn(List.of(itemDto));

        mvc.perform(get("/items/search")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 3L)
                        .param("text", "123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}