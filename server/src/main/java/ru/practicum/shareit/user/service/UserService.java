package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto saveUser(UserDto user);

    UserDto updateUser(Long userId, UserDto user);

    List<UserDto> getUsers();

    UserDto findUserById(Long userId);

    void deleteUserById(Long userId);
}
