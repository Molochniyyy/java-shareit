package ru.practicum.shareit.exceptions;

public class EmptyCommentException extends RuntimeException {
    public EmptyCommentException(String message) {
        super(message);
    }
}
