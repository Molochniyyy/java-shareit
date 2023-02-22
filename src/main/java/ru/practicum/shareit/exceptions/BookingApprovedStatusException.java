package ru.practicum.shareit.exceptions;

public class BookingApprovedStatusException extends RuntimeException{
    public BookingApprovedStatusException(String message) {
        super(message);
    }
}
