package ru.practicum.shareit.booking.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.exceptions.ValidationException;

import java.time.LocalDateTime;

public class BookingValidatorTest {
    @Test
    void validate() {
        BookingValidator validationService = new BookingValidator();
        LocalDateTime dateTime1 = LocalDateTime.of(2023, 2, 5, 11, 11);
        LocalDateTime dateTime2 = LocalDateTime.of(2023, 2, 6, 11, 11);
        BookingDtoRequest dto1 = new BookingDtoRequest(1L, dateTime2, dateTime1);
        Assertions.assertThrows(ValidationException.class, () -> validationService.isValidBooking(dto1));
        BookingDtoRequest dto2 = new BookingDtoRequest(1L, dateTime1, dateTime2);
        Assertions.assertDoesNotThrow(() -> validationService.isValidBooking(dto2));
    }
}
