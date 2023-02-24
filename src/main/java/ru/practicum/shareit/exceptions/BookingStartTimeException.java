package ru.practicum.shareit.exceptions;

public class BookingStartTimeException extends RuntimeException {
    public BookingStartTimeException(String message) {
        super(message);
    }
}
