package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.BookingStartTimeException;
import ru.practicum.shareit.exceptions.FailBookingException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class BookingValidator {
    public static void isValidBooking(Booking booking, User user, Item item) {
        if (user.getId().equals(item.getOwnerId())) {
            throw new FailBookingException("Владелец вещи не может ее забронировать");
        }
        if (item.getAvailable()) {
            if (booking.getStart().isAfter(booking.getEnd())
                    || booking.getEnd().isBefore(LocalDateTime.now())
                    || booking.getStart().isBefore(LocalDateTime.now())) {
                throw new BookingStartTimeException("Даты бронирования не верны");
            }
        }
    }
}
