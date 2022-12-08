package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {
    User add(User user);

    User update(Integer id, User user);

    List<User> get();

    User getById(Integer id);

    void delete(Integer id);
}
