package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.UnknownStateException;
import ru.practicum.shareit.exceptions.UnsupportedOperationException;

import java.util.Collection;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService service;

    @PostMapping
    public BookingDto addNewBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @Validated({Create.class}) @RequestBody BookingDtoRequest bookingDtoRequest) {
        return service.addBooking(userId, bookingDtoRequest);
    }

    /**
     * Подтверждение или отклонение запроса на бронирование.
     */
    @PatchMapping("/{bookingId}")
    public BookingDto updateBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @PathVariable Long bookingId,
                                    @RequestParam Boolean approved) {
        return service.updateBooking(userId, bookingId, approved);
    }

    /**
     * Получение данных о конкретном бронировании (включая его статус)
     */
    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable Long bookingId) {
        return service.getBookingById(userId, bookingId);
    }

    /**
     * Получение списка всех бронирований текущего пользователя
     */
    @GetMapping()
    public Collection<BookingDto> getBookingsByBookerId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                        @RequestParam(required = false,
                                                                defaultValue = "ALL") String state,
                                                        @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                        @RequestParam(name = "size", defaultValue = "20") Integer size) {
        if (from < 0 || size < 1) {
            throw new UnsupportedOperationException("Неверные параметры запроса");
        }
        BookingState bookingState = BookingState.from(state)
                .orElseThrow(() -> new UnknownStateException(state));
        return service.getBookingsByBookerId(userId, bookingState, from, size);
    }

    /**
     * Получение списка бронирований для всех вещей текущего пользователя
     */
    @GetMapping("/owner")
    public Collection<BookingDto> getBookingAllItemsByOwnerId(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                              @RequestParam(name = "state",
                                                                      defaultValue = "ALL") String state,
                                                              @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                              @RequestParam(name = "size", defaultValue = "20") Integer size) {
        if (from < 0 || size < 1) {
            throw new UnsupportedOperationException("Неверные параметры запроса");
        }
        BookingState bookingState = BookingState.from(state)
                .orElseThrow(() -> new UnknownStateException(state));
        return service.getBookingAllItemsByOwnerId(ownerId, bookingState, from, size);
    }
}
