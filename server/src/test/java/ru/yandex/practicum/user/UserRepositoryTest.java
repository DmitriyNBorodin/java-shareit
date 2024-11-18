package ru.yandex.practicum.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.yandex.practicum.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void findUserByEmailTest() {
        User testUser = User.builder()
                .name("name")
                .email("example@mail.com")
                .build();
        userRepository.save(testUser);
        Optional<User> expectedUser = userRepository.findUserByEmail("example@mail.com");

        assertTrue(expectedUser.isPresent());
        assertEquals("name", expectedUser.get().getName());
    }

    @Test
    public void findUserByIdTest() {
        User testUser = User.builder()
                .name("name")
                .email("example@mail.com")
                .build();
        User savedUser = userRepository.save(testUser);
        Optional<User> expectedUser = userRepository.findUserById(savedUser.getId());

        assertTrue(expectedUser.isPresent());
        assertEquals("name", expectedUser.get().getName());
    }
}
