package ru.practicum.shareit.exceptions;

public class FailEmailException extends RuntimeException{
    public FailEmailException(String message) {
        super(message);
    }
}
