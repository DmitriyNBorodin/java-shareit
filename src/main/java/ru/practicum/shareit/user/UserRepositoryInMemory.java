package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.exceptions.UserValidationException;
import ru.practicum.shareit.user.model.User;

import java.util.Map;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryInMemory {
    @Autowired
    private final Map<Long, User> userStorage;

    public User addUser(User user) {
        user.setId(generateNewId());
        userStorage.put(user.getId(), user);
        log.info("Добавлен пользователь с id {}", user.getId());
        return user;
    }

    public User getUserById(Long userId) {
        log.info("Получение пользователя по id {}", userId);
        return userStorage.get(userId);
    }

    public User updateUser(Long userId, User user) {
        userStorage.put(userId, user);
        log.info("Обновление данный о пользователе с id {}", user.getId());
        return user;
    }

    public void deleteUser(long userId) {
        userStorage.remove(userId);
        log.info("Удален пользователь с id {}", userId);
    }

    private Long generateNewId() {
        if (!userStorage.isEmpty()) {
            return userStorage.keySet().stream().max(Long::compare).get() + 1;
        } else return 1L;
    }

    public void validateEmail(String email) {
        if (userStorage.values().stream().anyMatch(user -> user.getEmail().equals(email))) {
            throw new UserValidationException("email " + email + " уже используется");
        }
        ;
    }

    public void validateUser(Long userId) {
        if (!userStorage.containsKey(userId)) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден");
        }
        ;
    }
}
