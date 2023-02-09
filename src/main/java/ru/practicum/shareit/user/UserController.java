package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;


@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    public List<UserDto> get() {
        return UserWrapper.toListOfDto(service.get());
    }

    @PostMapping
    public UserDto add(@RequestBody UserDto user) {
        return UserWrapper.toUserDto(service.add(UserWrapper.toUser(user)));
    }

    @PatchMapping("/{userId}")
    public UserDto update(@RequestBody UserDto user, @PathVariable Integer userId) {
        return UserWrapper.toUserDto(service.update(userId, UserWrapper.toUser(user)));
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Integer userId) {
        service.delete(userId);
    }

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable Integer userId) {
        return UserWrapper.toUserDto(service.getById(userId));
    }
}
