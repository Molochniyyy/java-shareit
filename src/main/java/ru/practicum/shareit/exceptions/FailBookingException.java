package ru.practicum.shareit.exceptions;

public class FailBookingException extends RuntimeException {
    public FailBookingException(String message) {
        super(message);
    }
}
