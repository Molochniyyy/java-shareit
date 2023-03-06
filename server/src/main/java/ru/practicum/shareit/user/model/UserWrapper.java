package ru.practicum.shareit.user.model;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class UserWrapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static User toUser(UserDto userDto) {
        return new User(userDto.getId(),
                userDto.getName(),
                userDto.getEmail()
        );
    }

    public static List<UserDto> toListOfDto(List<User> users) {
        List<UserDto> userDto = new ArrayList<>();
        for (User user : users) {
            userDto.add(toUserDto(user));
        }
        return userDto;
    }
}
