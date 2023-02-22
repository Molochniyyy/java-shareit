package ru.practicum.shareit.exceptions;

public class BookingNorFoundException extends RuntimeException{
    public BookingNorFoundException(String message) {
        super(message);
    }
}
