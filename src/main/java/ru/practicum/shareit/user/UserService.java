package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    UserDto saveUser(User user);

    UserDto updateUser(Long userId, User user);

    List<UserDto> getUsers();

    UserDto findUserById(Long userId);

    void deleteUserById(Long userId);
}
