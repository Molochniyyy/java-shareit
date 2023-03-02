package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.BookingWrapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.validator.BookingValidator;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.UnavailableItemException;
import ru.practicum.shareit.exceptions.UnknownStateException;
import ru.practicum.shareit.exceptions.UnsupportedOperationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository repository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    /*
    Добавление нового бронирования
     */
    @Override
    public BookingDto addBooking(Long userId, BookingDtoRequest bookingDtoRequest) {
        Item item = itemRepository.findById(bookingDtoRequest.getItemId())
                .orElseThrow(() -> new ObjectNotFoundException("Вещи не существует"));
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователя не существует"));

        Booking booking = BookingWrapper.toBooking(bookingDtoRequest, item, booker);

        BookingValidator.isValidBooking(booking, booker, item);

        if (item.getAvailable()) {
            return BookingWrapper.toBookingDto(repository.save(booking));
        } else {
            throw new UnavailableItemException("Товар недоступен");
        }
    }

    /*
    Подтверждение или отказ запроса на бронирование
     */
    @Override
    public BookingDto updateBooking(Long userId, Long bookingId, Boolean approved) {
        Booking booking = repository.findById(bookingId).orElseThrow(
                () -> new ObjectNotFoundException("Не найдена аренда с id " + bookingId)
        );
        Item item = booking.getItem();
        if (!item.getOwner().getId().equals(userId)) {
            throw new ObjectNotFoundException("Id пользователя и владельца не совпадают");
        }
        if (!booking.getStatus().equals(BookingStatus.WAITING)) {
            throw new UnsupportedOperationException("Попытка изменить статус отличный от WAITING");
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return BookingWrapper.toBookingDto(repository.save(booking));
    }

    /*
    Получение бронирования по id
     */
    @Override
    public BookingDto getBookingById(Long userId, Long bookingId) {
        Booking booking = repository.findById(bookingId)
                .orElseThrow(() -> new ObjectNotFoundException("Бронирование не найдено"));
        Item item = booking.getItem();
        if (Objects.equals(booking.getBooker().getId(), userId) || item.getOwner().getId().equals(userId)) {
            return BookingWrapper.toBookingDto(booking);
        } else {
            throw new ObjectNotFoundException("Пользователь не является владельцем вещи или автороом бронирования");
        }
    }

    /*
    Получение списка бронированных вещей пользователем
     */

    @Override
    public Collection<BookingDto> getBookingsByBookerId(Long userId, BookingState state, Integer from, Integer size) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException("Пользователь не найден")
        );
        Pageable pageable = PageRequest.of(from / size, size);
        Page<Booking> bookings;
        switch (state) {
            case ALL:
                bookings = repository.findAllByBookerOrderByStartDesc(user, pageable);
                break;
            case PAST:
                bookings = repository.findAllByBookerAndEndBeforeOrderByStartDesc(user, LocalDateTime.now(), pageable);
                break;
            case FUTURE:
                bookings = repository.findAllByBookerAndStartAfterOrderByStartDesc(user, LocalDateTime.now(), pageable);
                break;
            case CURRENT:
                bookings = repository.findBookingByBookerAndDate(user, LocalDateTime.now(), pageable);
                break;
            case REJECTED:
                bookings = repository.findAllByBookerAndStatusOrderByStartDesc(user,
                        BookingStatus.REJECTED, pageable);
                break;
            case WAITING:
                bookings = repository.findAllByBookerAndStatusOrderByStartDesc(user,
                        BookingStatus.WAITING,
                        pageable);
                break;
            default:
                throw new UnknownStateException("Unknown state: "+ state);

        }
        return bookings.stream()
                .map(BookingWrapper::toBookingDto)
                .collect(Collectors.toList());
    }

    /*
    Получение списка бронирований по каждой вещи пользователя
     */
    @Override
    public List<BookingDto> getBookingAllItemsByOwnerId(Long userId, BookingState state, Integer from, Integer size) {
        User owner = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException("Не найден пользователь с id " + userId)
        );
        Pageable pageable = PageRequest.of(from / size, size);
        Page<Booking> bookings;
        switch (state) {
            case ALL:
                bookings = repository.findAllByItemOwnerIsOrderByStartDesc(owner, pageable);
                break;
            case CURRENT:
                bookings = repository.findBookingByOwnerAndDate(owner, LocalDateTime.now(), pageable);
                break;
            case PAST:
                bookings = repository.findAllByItemOwnerIsAndEndBeforeOrderByStartDesc(owner,
                        LocalDateTime.now(),
                        pageable);
                break;
            case FUTURE:
                bookings = repository.findAllByItemOwnerIsAndStartAfterOrderByStartDesc(owner,
                        LocalDateTime.now(),
                        pageable);
                break;
            case WAITING:
                bookings = repository.findAllByItemOwnerIsAndStatusOrderByStartDesc(owner,
                        BookingStatus.WAITING,
                        pageable);
                break;
            case REJECTED:
                bookings = repository.findAllByItemOwnerIsAndStatusOrderByStartDesc(owner,
                        BookingStatus.REJECTED,
                        pageable);
                break;
            default:
                throw new UnknownStateException("Unknown state: "+ state);
        }
        return bookings.stream()
                .map(BookingWrapper::toBookingDto)
                .collect(Collectors.toList());
    }


}
