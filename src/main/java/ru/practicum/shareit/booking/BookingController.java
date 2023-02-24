package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService service;

    @PostMapping
    public BookingDto addNewBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @Valid @RequestBody Booking booking) {
        return service.addBooking(userId, booking);
    }

    /**
     * Подтверждение или отклонение запроса на бронирование.
     */
    @PatchMapping("/{bookingId}")
    public BookingDto updateBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @PathVariable Long bookingId,
                                    @RequestParam String approved) {
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
    public List<BookingDto> getBookingsByBookerId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @RequestParam(required = false) String state) {
        return service.getBookingsByBookerId(userId, Objects.requireNonNullElse(state, "ALL"));
    }

    /**
     * Получение списка бронирований для всех вещей текущего пользователя
     */
    @GetMapping("/owner")
    public List<BookingDto> getBookingAllItemsByOwnerId(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                        @RequestParam(required = false, name = "state") String state) {
        return service.getBookingAllItemsByOwnerId(ownerId, Objects.requireNonNullElse(state, "ALL"));
    }
}
