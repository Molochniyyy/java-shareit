package ru.practicum.shareit.booking.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.exceptions.UnknownStateException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> getBookingsOfUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getBookingsOfUser(userId, state, from, size);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> patchBooking(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                               @PathVariable Long bookingId,
                                               @RequestParam(name = "approved") Boolean isApproved) {
        return bookingClient.patchBooking(userId, bookingId, isApproved);
    }

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @RequestBody @Valid BookingDtoRequest requestDto) {
        log.info("Creating booking {}, userId={}", requestDto, userId);
        return bookingClient.createBooking(userId, requestDto);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }
    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsOfUserItems(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                                         @RequestParam(name = "state",
                                                                 defaultValue = "ALL") String state,
                                                         @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                         @RequestParam(name = "size", defaultValue = "20") Integer size) {
        if (from < 0 || size < 1) {
            throw new UnsupportedOperationException("Неверные параметры запроса");
        }
        BookingState bookingState = BookingState.from(state)
                .orElseThrow(() -> new UnknownStateException(state));

        return bookingClient.getBookingsOfUserItems(userId, bookingState, from, size);
    }
}
