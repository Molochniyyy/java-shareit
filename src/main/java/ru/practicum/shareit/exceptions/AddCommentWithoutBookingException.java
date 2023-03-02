package ru.practicum.shareit.exceptions;

public class AddCommentWithoutBookingException extends RuntimeException {
    public AddCommentWithoutBookingException(String message) {
        super(message);
    }
}
