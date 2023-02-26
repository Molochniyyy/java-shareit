package ru.practicum.shareit.booking;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;

import java.util.List;

@Service
public interface BookingService {

    BookingDto addBooking(Long userId, BookingDtoRequest bookingDtoRequest);

    BookingDto updateBooking(Long userId, Long bookingId, String approved);

    BookingDto getBookingById(Long userId, Long bookingId);

    List<BookingDto> getBookingsByBookerId(Long userId, String state);

    List<BookingDto> getBookingAllItemsByOwnerId(Long userId, String state);
}
