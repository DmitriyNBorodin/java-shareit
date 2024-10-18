package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserValidator {
    private final UserRepository userRepository;

    public void checkUserOnExistence(Long userId) throws UserNotFoundException {
        if (!userRepository.validateUser(userId))
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден");
    }

    public void validateUserEmail(String email) throws UserValidationException {
        if (userRepository.validateEmail(email))
            throw new UserValidationException("email " + email + " уже используется");
    }
}
