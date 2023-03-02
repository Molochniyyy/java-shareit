package ru.practicum.shareit.booking.validator;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class BookingValidator {
    public static void isValidBooking(Booking booking, User user, Item item) {
        if (item.getAvailable()) {
            if (booking.getStart().isAfter(booking.getEnd())
                    || booking.getEnd().isBefore(LocalDateTime.now())
                    || booking.getStart().isBefore(LocalDateTime.now())) {
                throw new ValidationException("Даты бронирования не верны");
            }
        }
        if (user.getId().equals(item.getOwner().getId())) {
            throw new ObjectNotFoundException("Владелец вещи не может ее забронировать");
        }

    }
}
