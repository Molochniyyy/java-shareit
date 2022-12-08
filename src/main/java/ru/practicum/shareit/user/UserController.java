package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    public List<User> get() {
        return service.get();
    }

    @PostMapping
    public User add(@RequestBody User user) {
        return service.add(user);
    }

    @PatchMapping("/{userId}")
    public User update(@RequestBody User user, @PathVariable Integer userId) {
        return service.update(userId, user);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Integer userId) {
        service.delete(userId);
    }

    @GetMapping("/{userId}")
    public User getById(@PathVariable Integer userId) {
        return service.getById(userId);
    }
}
