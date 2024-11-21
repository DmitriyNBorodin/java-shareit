package ru.yandex.practicum.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import ru.yandex.practicum.ClientTestConfig;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitWebConfig({UserClient.class, ClientTestConfig.class})
@ExtendWith(MockitoExtension.class)
class UserClientTest {
    @Autowired
    private final UserClient userClient = new UserClient("/users", new RestTemplateBuilder());
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
    void addUser() throws JsonProcessingException, InterruptedException {
        UserDto newUser = UserDto.builder().name("name").email("1@mail.com").build();
        UserDto userForResponse = UserDto.builder().id(1L).name("name").email("1@mail.com").build();
        MockResponse mockResponse = new MockResponse();
        mockResponse.setResponseCode(200);
        mockResponse.addHeader("Content-Type", "application/json");
        mockResponse.setBody(mapper.writeValueAsString(userForResponse));
        mockWebServer.enqueue(mockResponse);
        ResponseEntity<Object> savedUser = userClient.addUser(newUser);
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        UserDto savedUserDto = mapper.convertValue(savedUser.getBody(), UserDto.class);
        assertEquals("name", savedUserDto.getName());
        assertEquals(1L, savedUserDto.getId());
        assertEquals("POST", recordedRequest.getMethod());
    }

    @Test
    void getUserById() throws JsonProcessingException, InterruptedException {
        UserDto userForResponse = UserDto.builder().id(1L).name("name").email("1@mail.com").build();
        MockResponse mockResponse = new MockResponse();
        mockResponse.setResponseCode(200);
        mockResponse.addHeader("Content-Type", "application/json");
        mockResponse.setBody(mapper.writeValueAsString(userForResponse));
        mockWebServer.enqueue(mockResponse);
        ResponseEntity<Object> requiredUser = userClient.getUserById(1L);
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        UserDto requiredUserDto = mapper.convertValue(requiredUser.getBody(), UserDto.class);
        assertEquals("1@mail.com", requiredUserDto.getEmail());
        assertEquals(1L, requiredUserDto.getId());
        assertEquals("GET", recordedRequest.getMethod());
    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }
}