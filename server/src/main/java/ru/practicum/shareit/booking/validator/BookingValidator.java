package ru.practicum.shareit.booking.validator;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.exceptions.ValidationException;


@Service
public class BookingValidator {
    public void isValidBooking(BookingDtoRequest bookingDtoRequest) {
        if (!bookingDtoRequest.getStart().isBefore(bookingDtoRequest.getEnd())) {
            throw new ValidationException("Время начала должно быть раньше времени окончания аренды");
        }
    }
}
