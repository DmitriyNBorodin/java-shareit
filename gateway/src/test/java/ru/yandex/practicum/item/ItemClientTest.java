package ru.yandex.practicum.item;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import ru.yandex.practicum.ClientTestConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitWebConfig({ItemClient.class, ClientTestConfig.class})
@ExtendWith(MockitoExtension.class)
class ItemClientTest {
    @Autowired
    private final ItemClient itemClient = new ItemClient("/items", new RestTemplateBuilder());
    public static MockWebServer mockWebServer = new MockWebServer();
    private final ObjectMapper mapper = new ObjectMapper();


    @BeforeAll
    public static void setUp() throws IOException {
        mockWebServer.start(9090);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void addNewItemTest() throws IOException, InterruptedException {
        ItemDto itemDto = ItemDto.builder().name("name")
                .description("description").available(true).build();
        ItemDto answerDto = ItemDto.builder().id(1L).name("name")
                .description("description").available(true).build();
        MockResponse mockResponse = new MockResponse();
        mockResponse.setResponseCode(200);
        mockResponse.addHeader("Content-Type", "application/json");
        mockResponse.setBody(mapper.writeValueAsString(answerDto));
        mockWebServer.enqueue(mockResponse);
        ResponseEntity<Object> savedItem = itemClient.addNewItem(1L, itemDto);
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        ItemDto savedItemDto = mapper.convertValue(savedItem.getBody(), ItemDto.class);
        assertEquals("name", savedItemDto.getName());
        assertEquals(1L, savedItemDto.getId());
        assertEquals("POST", recordedRequest.getMethod());
    }

    @Test
    void addNewItemComment() throws JsonProcessingException, InterruptedException {
        CommentDto commentDto = CommentDto.builder()
                .text("123").build();
        CommentDto answerDto = CommentDto.builder()
                .id(1L).text("123").build();
        MockResponse mockResponse = new MockResponse();
        mockResponse.setResponseCode(200);
        mockResponse.addHeader("Content-Type", "application/json");
        mockResponse.setBody(mapper.writeValueAsString(answerDto));
        mockWebServer.enqueue(mockResponse);
        ResponseEntity<Object> savedComment = itemClient.addNewItemComment(1L, 1L, commentDto);
        CommentDto savedCommentDto = mapper.convertValue(savedComment.getBody(), CommentDto.class);
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("123", savedCommentDto.getText());
        assertEquals(1L, savedCommentDto.getId());
        assertEquals("POST", recordedRequest.getMethod());
    }

    @Test
    void updateItem() throws JsonProcessingException, InterruptedException {
        ItemDto itemDto = ItemDto.builder().id(1L).name("name")
                .description("description").available(true).build();
        ItemDto answerDto = ItemDto.builder().id(1L).name("name")
                .description("updatedDescription").available(true).build();
        MockResponse mockResponse = new MockResponse();
        mockResponse.setResponseCode(200);
        mockResponse.addHeader("Content-Type", "application/json");
        mockResponse.setBody(mapper.writeValueAsString(answerDto));
        mockWebServer.enqueue(mockResponse);
        ResponseEntity<Object> savedItem = itemClient.updateItem(1L, itemDto, 1L);
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        ItemDto savedItemDto = mapper.convertValue(savedItem.getBody(), ItemDto.class);
        assertEquals("updatedDescription", savedItemDto.getDescription());
        assertEquals(HttpStatus.OK, savedItem.getStatusCode());
        assertEquals("PATCH", recordedRequest.getMethod());
    }

    @Test
    void getItemById() throws JsonProcessingException, InterruptedException {
        ItemDto itemDto = ItemDto.builder().id(1L).name("name")
                .description("description").available(true).build();
        MockResponse mockResponse = new MockResponse();
        mockResponse.setResponseCode(200);
        mockResponse.addHeader("Content-Type", "application/json");
        mockResponse.setBody(mapper.writeValueAsString(itemDto));
        mockWebServer.enqueue(mockResponse);
        ResponseEntity<Object> requiredItem = itemClient.getItemById(1L);
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        ItemDto requiredItemDto = mapper.convertValue(requiredItem.getBody(), ItemDto.class);
        assertEquals("name", requiredItemDto.getName());
        assertEquals(HttpStatus.OK, requiredItem.getStatusCode());
        assertEquals("GET", recordedRequest.getMethod());
    }

    @Test
    void getItemsByOwnerId() throws JsonProcessingException, InterruptedException {
        ItemDto item1Dto = ItemDto.builder().id(1L).name("name1")
                .description("description1").available(true).build();
        ItemDto item2Dto = ItemDto.builder().id(2L).name("name2")
                .description("description2").available(true).build();
        List<ItemDto> itemList = new ArrayList<>(Arrays.asList(item1Dto, item2Dto));
        MockResponse mockResponse = new MockResponse();
        mockResponse.setResponseCode(200);
        mockResponse.addHeader("Content-Type", "application/json");
        mockResponse.setBody(mapper.writeValueAsString(itemList));
        mockWebServer.enqueue(mockResponse);
        ResponseEntity<Object> userItemList = itemClient.getItemsByOwnerId(1L);
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        List userItemListDto = mapper.convertValue(userItemList.getBody(), List.class);
        assertEquals(2, userItemListDto.size());
        assertEquals(HttpStatus.OK, userItemList.getStatusCode());
        assertEquals("GET", recordedRequest.getMethod());
    }

    @Test
    void searchForItems() throws JsonProcessingException, InterruptedException {
        ItemDto item1Dto = ItemDto.builder().id(1L).name("name1")
                .description("description1").available(true).build();
        ItemDto item2Dto = ItemDto.builder().id(2L).name("name2")
                .description("description2").available(true).build();
        List<ItemDto> itemList = new ArrayList<>(Arrays.asList(item1Dto, item2Dto));
        MockResponse mockResponse = new MockResponse();
        mockResponse.setResponseCode(200);
        mockResponse.addHeader("Content-Type", "application/json");
        mockResponse.setBody(mapper.writeValueAsString(itemList));
        mockWebServer.enqueue(mockResponse);
        ResponseEntity<Object> userItemList = itemClient.searchForItems("descr");
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        List userItemListDto = mapper.convertValue(userItemList.getBody(), List.class);
        assertEquals(2, userItemListDto.size());
        assertEquals(HttpStatus.OK, userItemList.getStatusCode());
        assertEquals("GET", recordedRequest.getMethod());
        assertTrue(recordedRequest.getPath().contains("descr"));
    }
}