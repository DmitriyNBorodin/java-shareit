package ru.yandex.practicum.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.user.dto.UserDto;
import ru.yandex.practicum.user.model.User;

@Component
public class UserDtoMapper {
    public User dtoToUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    public UserDto userToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
