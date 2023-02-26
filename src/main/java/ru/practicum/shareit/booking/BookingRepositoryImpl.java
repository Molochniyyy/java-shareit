package ru.practicum.shareit.booking;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingWrapper;
import ru.practicum.shareit.exceptions.BookingApprovedStatusException;
import ru.practicum.shareit.exceptions.BookingNotFoundException;
import ru.practicum.shareit.exceptions.OwnerNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Component
public class BookingRepositoryImpl {
    private final BookingRepository repository;

    public BookingRepositoryImpl(@Lazy BookingRepository repository) {
        this.repository = repository;
    }

    public BookingDto update(Long userId, Long bookingId, String approved, User booker, Item item) {
        Booking booking = repository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Такого бронирования не существует"));
        if (booking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new BookingApprovedStatusException("Бронирование уже одобрено");
        } else {
            if (item.getOwnerId().equals(userId)) {
                if (approved.equals("true")) {
                    booking.setStatus(BookingStatus.APPROVED);
                    item.setAvailable(true);
                } else {
                    booking.setStatus(BookingStatus.REJECTED);
                    item.setAvailable(true);
                }
                repository.save(booking);
            } else {
                throw new OwnerNotFoundException("Пользователь не является владельцем вещи");
            }
            return BookingWrapper.toBookingDtoForUpdate(booking, item, booker);
        }
    }
}
