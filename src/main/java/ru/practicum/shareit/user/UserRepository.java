package ru.practicum.shareit.user;

import java.util.List;

public interface UserRepository {

    User add(User user);

    User update(Integer id, User user);

    User getById(Integer id);

    void delete(Integer id);

    List<User> get();
}
