package ru.practicum.shareit.booking;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

@Service
public interface BookingService {

    BookingDto addBooking(Long userId, Booking booking);

    BookingDto updateBooking(Long userId, Long bookingId, String approved);

    BookingDto getBookingById(Long userId, Long bookingId);

    List<BookingDto> getBookingsByBookerId(Long userId, String state);

    List<BookingDto> getBookingAllItemsByOwnerId(Long userId, String all);
}
