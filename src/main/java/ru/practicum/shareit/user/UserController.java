package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.UserWrapper;

import java.util.List;


@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping
    public UserDto save(@RequestBody UserDto user) {
        return service.saveUser(UserWrapper.toUser(user));
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable Long userId, @RequestBody UserDto user) {
        return service.updateUser(userId, UserWrapper.toUser(user));
    }

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable Long userId) {
        return service.findUserById(userId);
    }

    @GetMapping
    public List<UserDto> getAll() {
        return service.getUsers();
    }

    @DeleteMapping("/{userId}")
    public void deleteById(@PathVariable Long userId) {
        service.deleteUserById(userId);
    }
}
