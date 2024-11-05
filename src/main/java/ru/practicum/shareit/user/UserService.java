package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;

    public UserDto addUser(UserDto userDto) {
        User newUser = userDtoMapper.dtoToUser(userDto);
        userRepository.validateEmail(newUser.getEmail());
        return userDtoMapper.userToDto(userRepository.addUser(newUser));
    }

    public UserDto getUserById(Long userId) {
        return userDtoMapper.userToDto(userRepository.getUserById(userId));
    }

    public UserDto updateUser(Long userId, UserDto userDto) {
        User updatingUser = userRepository.getUserById(userId);
        if (userDto.getName() != null) {
            updatingUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            userRepository.validateEmail(userDto.getEmail());
            updatingUser.setEmail(userDto.getEmail());
        }
        return userDtoMapper.userToDto(userRepository.updateUser(userId, updatingUser));
    }

    public void deleteUser(Long userId) {
        userRepository.deleteUser(userId);
    }
}
