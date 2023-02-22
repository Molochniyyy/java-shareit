package ru.practicum.shareit.booking;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingWrapper;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

@Component
public class BookingRepositoryImpl {
    final private BookingRepository repository;

    final private ItemRepository itemRepository;

    final private UserRepository userRepository;

    public BookingRepositoryImpl(@Lazy BookingRepository repository, @Lazy ItemRepository itemRepository,
                                 UserRepository userRepository) {
        this.repository = repository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    public BookingDto update(Long userId, Long bookingId, String approved) {
        Booking booking = repository.findById(bookingId)
                .orElseThrow(() -> new BookingNorFoundException("Такого бронирования не существует"));
        if (booking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new BookingApprovedStatusException("Бронирование уже одобрено");
        } else {
            User booker = userRepository.findById(booking.getBookerId())
                    .orElseThrow(() -> new UserNotFoundException("Пользователя не существует"));
            Item item = itemRepository.findById(booking.getItemId())
                    .orElseThrow(() -> new ItemNotFoundException("Вещи не существует"));
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
            return BookingWrapper.toBookingDto(booking, item, booker);
        }
    }
}
