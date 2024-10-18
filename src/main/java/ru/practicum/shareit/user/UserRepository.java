package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepository {
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
        log.info("Удален пользователь с id {}", userId);
        userStorage.remove(userId);
    }

    private Long generateNewId() {
        if (!userStorage.isEmpty()) {
            return userStorage.keySet().stream().max(Long::compare).get() + 1;
        } else return 1L;
    }

    public boolean validateEmail(String email) {
        return userStorage.values().stream().anyMatch(user -> user.getEmail().equals(email));
    }

    public boolean validateUser(Long userId) {
        return userStorage.containsKey(userId);
    }
}
