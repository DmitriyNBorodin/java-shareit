package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserValidator userValidator;

    public User addUser(User user) {
        userValidator.validateUserEmail(user.getEmail());
        return userRepository.addUser(user);
    }

    public User getUserById(Long userId) {
        return userRepository.getUserById(userId);
    }

    public User updateUser(Long userId, User user) {
        User updatingUser = userRepository.getUserById(userId);
        if (user.getName() != null) updatingUser.setName(user.getName());
        if (user.getEmail() != null) {
            userValidator.validateUserEmail(user.getEmail());
            updatingUser.setEmail(user.getEmail());
        }
        return userRepository.updateUser(userId, updatingUser);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteUser(userId);
    }
}
