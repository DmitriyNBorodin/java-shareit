package ru.yandex.practicum.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.user.dto.UserDto;
import ru.yandex.practicum.user.exceptions.UserNotFoundException;
import ru.yandex.practicum.user.exceptions.UserValidationException;
import ru.yandex.practicum.user.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private UserService userService;
    UserDtoMapper userDtoMapper = new UserDtoMapper();
    @Mock
    private UserRepository userRepository;
    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @BeforeEach
    public void setUp() {
        userService = new UserService(userRepository, userDtoMapper);
    }

    @Test
    void addUserTest() {
        UserDto user = UserDto.builder().name("user").email("random@email.com").build();
        User controlUser = userDtoMapper.dtoToUser(user);
        controlUser.setId(1L);
        when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(controlUser);

        UserDto savedUser = userService.addUser(user);

        assertNotNull(savedUser);
        verify(userRepository).save(ArgumentMatchers.any(User.class));
        verify(userRepository).findUserByEmail(Mockito.anyString());
        assertEquals(1L, savedUser.getId());
    }

    @Test
    void getUserByIdTest() {
        User controlUser = User.builder()
                .name("name")
                .email("example@mail.com")
                .build();
        when(userRepository.getReferenceById(Mockito.anyLong())).thenReturn(controlUser);

        UserDto extractingUser = userService.getUserById(1L);

        assertNotNull(extractingUser);
        assertEquals("name", extractingUser.getName());
        assertEquals("example@mail.com", extractingUser.getEmail());
    }

    @Test
    void updateUserTest() {
        User controlUser = new User(1L, "name", "1@mail.com");
        when(userRepository.getReferenceById(Mockito.anyLong())).thenReturn(controlUser);
        when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(controlUser);
        UserDto controlDto1 = UserDto.builder()
                .name("name1")
                .build();

        userService.updateUser(1L, controlDto1);
        verify(userRepository).save(userArgumentCaptor.capture());
        User updatedUser1 = userArgumentCaptor.getValue();

        assertEquals("name1", updatedUser1.getName());
        assertEquals("1@mail.com", updatedUser1.getEmail());
    }

    @Test
    void deleteUserTest() {
        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void validateUserTest() {
        when(userRepository.findUserById(Mockito.anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.validateUser(1L));
    }

    @Test
    void validateEmailTest() {
        when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(Optional.of(new User()));
        UserDto newUser = UserDto.builder()
                .name("name")
                .email("e@mail.com")
                .build();
        assertThrows(UserValidationException.class, () -> userService.addUser(newUser));
        verify(userRepository, never()).save(new User());
    }
}