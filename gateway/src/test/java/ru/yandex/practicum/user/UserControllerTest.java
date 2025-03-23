package ru.yandex.practicum.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserClient userClient;
    @Autowired
    private ObjectMapper mapper;


    @Test
    void addUserTest() throws Exception {
        UserDto newUser = UserDto.builder().name("name").email("1@mail.com").build();
        when(userClient.addUser(Mockito.any(UserDto.class))).thenAnswer(
                invocationOnMock -> {
                    UserDto savedUser = invocationOnMock.getArgument(0, UserDto.class);
                    savedUser.setId(1L);
                    return new ResponseEntity<>(savedUser, HttpStatus.OK);
                }
        );

        mvc.perform(post("/users").content(mapper.writeValueAsString(newUser))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is(newUser.getName()), String.class))
                .andExpect(jsonPath("$.email", is(newUser.getEmail()), String.class));
    }

    @Test
    void getUserByIdTest() throws Exception {
        UserDto expectedUser = UserDto.builder().id(1L).name("name").email("1@mail.com").build();
        when(userClient.getUserById(Mockito.anyLong())).thenReturn(new ResponseEntity<>(expectedUser, HttpStatus.OK));

        mvc.perform(get("/users/{id}", 1L).characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is(expectedUser.getName()), String.class))
                .andExpect(jsonPath("$.email", is(expectedUser.getEmail()), String.class));
    }

    @Test
    void updateUserTest() throws Exception {
        UserDto user = UserDto.builder().id(1L).name("name").email("1@mail.com").build();
        when(userClient.updateUser(Mockito.anyLong(), Mockito.any(UserDto.class)))
                .thenAnswer(invocationOnMock -> {
                    UserDto updatingUser = invocationOnMock.getArgument(1, UserDto.class);
                    updatingUser.setName("name1");
                    updatingUser.setEmail("2@mail.com");
                    return new ResponseEntity<>(updatingUser, HttpStatus.OK);
                });

        mvc.perform(patch("/users/{id}", 1L).content(mapper.writeValueAsString(user))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is("name1"), String.class))
                .andExpect(jsonPath("$.email", is("2@mail.com"), String.class));
    }

    @Test
    void deleteUserByIdTest() throws Exception {
        mvc.perform(delete("/users/{id}", 1L))
                .andExpect(status().isOk());
        verify(userClient).deleteUser(1L);
    }
}