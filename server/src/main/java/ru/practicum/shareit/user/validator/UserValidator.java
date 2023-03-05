package ru.practicum.shareit.user.validator;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;

@Slf4j
public class UserValidator {

    public static void isValidIdUsers(long id) {
        if (id < 0) {
            log.warn("Id пользователя %d отрицательный");
            throw new ObjectNotFoundException(String.format("Id пользователя %d отрицательный", id));
        }
    }
}
