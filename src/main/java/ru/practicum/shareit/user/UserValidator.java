package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.model.User;

@Slf4j
public class UserValidator {
    public static void isValidUpdateUser(User user) throws ValidationException {
        if (user.getEmail() == null && user.getName() == null) {
            log.warn("Ошибка в email: {}", user);
            throw new ValidationException("Пользователь не соответствует условиям: " +
                    "и email и name не могут быть = Null");
        }
        isValidIdUsers(user.getId());
    }

    public static void isValidIdUsers(long id) {
        if (id < 0) {
            log.warn("Id пользователя %d отрицательный");
            throw new UserNotFoundException(String.format("Id пользователя %d отрицательный", id));
        }
    }

    public static void isValidEmailUser(User user) throws ValidationException {
        if (user.getEmail().isBlank()) {
            log.warn("Ошибка в email: {}", user);
            throw new ValidationException("Пользователь не соответствует условиям: " +
                    "email не должен быть пустым");
        }
        if (!user.getEmail().contains("@")) {
            log.warn("Ошибка в email: {}", user);
            throw new ValidationException("Пользователь не соответствует условиям: " +
                    "email должен содержать - \"@\"");
        }
    }

    public static void isValidUserToNull(User user) {
        if (user.getEmail() == null) {
            log.warn("Ошибка в email: {}", user);
            throw new ValidationException("Пользователь не соответствует условиям: " +
                    "email должен быть");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("Имя пусто - заменено логином: {}", user);
            throw new ValidationException("Пользователь не соответствует условиям: " +
                    "name не должен быть пустым");
        }
    }
}
