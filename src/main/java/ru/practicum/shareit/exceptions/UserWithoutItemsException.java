package ru.practicum.shareit.exceptions;

public class UserWithoutItemsException extends RuntimeException {
    public UserWithoutItemsException(String message) {
        super(message);
    }
}
