package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.Collection;

@Service
public interface BookingService {

    BookingDto addBooking(Long userId, BookingDtoRequest bookingDtoRequest);

    BookingDto updateBooking(Long userId, Long bookingId, Boolean approved);

    BookingDto getBookingById(Long userId, Long bookingId);

    Collection<BookingDto> getBookingsByBookerId(Long userId, BookingState state, Integer from, Integer size);

    Collection<BookingDto> getBookingAllItemsByOwnerId(Long userId, BookingState state, Integer from, Integer size);
}
