package ru.yandex.practicum.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.user.dto.UserDto;
import ru.yandex.practicum.user.exceptions.UserNotFoundException;
import ru.yandex.practicum.user.exceptions.UserValidationException;
import ru.yandex.practicum.user.model.User;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserDtoMapper userDtoMapper) {
        this.userRepository = userRepository;
        this.userDtoMapper = userDtoMapper;
    }

    public UserDto addUser(UserDto userDto) {
        User newUser = userDtoMapper.dtoToUser(userDto);
        validateEmail(newUser.getEmail());
        return userDtoMapper.userToDto(userRepository.save(newUser));
    }

    public UserDto getUserById(Long userId) {
        return userDtoMapper.userToDto(userRepository.getReferenceById(userId));
    }

    public UserDto updateUser(Long userId, UserDto userDto) {
        User updatingUser = userRepository.getReferenceById(userId);
        if (userDto.getName() != null) {
            updatingUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            validateEmail(userDto.getEmail());
            updatingUser.setEmail(userDto.getEmail());
        }
        return userDtoMapper.userToDto(userRepository.save(updatingUser));
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public void validateUser(Long userId) {
        if (userRepository.findUserById(userId).isEmpty()) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден");
        }
        ;
    }

    private void validateEmail(String email) {
        if (userRepository.findUserByEmail(email).isPresent()) {
            throw new UserValidationException("Почта уже используется");
        }
    }
}
