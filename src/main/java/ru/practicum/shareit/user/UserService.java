package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User addUser(User user) {
        userRepository.validateEmail(user.getEmail());
        return userRepository.addUser(user);
    }

    public User getUserById(Long userId) {
        return userRepository.getUserById(userId);
    }

    public User updateUser(Long userId, User user) {
        User updatingUser = userRepository.getUserById(userId);
        if (user.getName() != null) {
            updatingUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            userRepository.validateEmail(user.getEmail());
            updatingUser.setEmail(user.getEmail());
        }
        return userRepository.updateUser(userId, updatingUser);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteUser(userId);
    }
}
