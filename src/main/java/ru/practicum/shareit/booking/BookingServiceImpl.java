package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingWrapper;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository repository;
    private final BookingRepositoryImpl repositoryImpl;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    /*
    Добавление нового бронирования
     */
    @Override
    public BookingDto addBooking(Long userId, Booking booking) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователя не существует"));
        Item item = itemRepository.findById(booking.getItemId())
                .orElseThrow(() -> new ItemNotFoundException("Вещи не существует"));

        BookingValidator.isValidBooking(booking, user, item);

        if (item.getAvailable()) {
            booking.setStatus(BookingStatus.WAITING);
            booking.setBookerId(userId);
            item.setAvailable(false);
            Booking newBooking = repository.save(booking);
            return BookingWrapper.toBookingDto(newBooking, item, user);
        } else {
            throw new ItemNotAvailableException("Товар недоступен");
        }
    }

    /*
    Подтверждение или отказ запроса на бронирование
     */
    @Override
    public BookingDto updateBooking(Long userId, Long bookingId, String approved) {
        return repositoryImpl.update(userId, bookingId, approved);
    }

    /*
    Получение бронирования по id
     */
    @Override
    public BookingDto getBookingById(Long userId, Long bookingId) {
        Booking booking = repository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Бронирование не найдено"));
        Item item = itemRepository.findById(booking.getItemId())
                .orElseThrow(() -> new ItemNotFoundException("Вещь не найдена"));
        User booker = userRepository.findById(booking.getBookerId())
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        if (Objects.equals(booking.getBookerId(), userId) || item.getOwnerId().equals(userId)) {
            return BookingWrapper.toBookingDto(booking, item, booker);
        } else {
            throw new UserNotFoundException("Пользователь не является владельцем вещи или автороом бронирования");
        }
    }

    /*
    Получение списка бронированных вещей пользователем
     */

    @Override
    public List<BookingDto> getBookingsByBookerId(Long userId, String state) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        List<Booking> bookingsOfBooker = repository.findBookingByBookerId(userId);
        List<Booking> bookingByState = getBookingsByState(state, bookingsOfBooker);
        Collections.sort(bookingByState);
        return getListBookingDto(bookingByState);
    }

    /*
    Получение списка бронирований по каждой вещи пользователя
     */
    @Override
    public List<BookingDto> getBookingAllItemsByOwnerId(Long userId, String state) {
        List<Item> items = itemRepository.findItemsByOwnerId(userId);

        if (!items.isEmpty()) {
            List<Booking> bookings = new ArrayList<>(Collections.emptyList());
            for (Item item : items) {
                List<Booking> bookingsOfItem = repository.findBookingByItemId(item.getId());
                bookings.addAll(bookingsOfItem);
            }
            List<Booking> bookingsByState = getBookingsByState(state, bookings);
            Collections.sort(bookingsByState);
            return getListBookingDto(bookingsByState);
        } else {
            throw new UserWithoutItemsException("У пользователя нет вещей");
        }
    }

    private List<BookingDto> getListBookingDto(List<Booking> bookings) {
        List<BookingDto> result = new ArrayList<>();

        for (Booking booking : bookings) {
            Item item = itemRepository.findById(booking.getItemId()).orElseThrow();
            User booker = userRepository.findById(booking.getBookerId()).orElseThrow();
            result.add(BookingWrapper.toBookingDto(booking, item, booker));
        }

        return result;
    }

    private List<Booking> getBookingsByState(String state, List<Booking> bookings) {
        List<Booking> result = new ArrayList<>();
        switch (state) {
            case ("CURRENT"):
                for (Booking booking : bookings) {
                    if (booking.getStart().isBefore(LocalDateTime.now()) && booking.getEnd().isAfter(LocalDateTime.now())) {
                        result.add(booking);
                    }
                }
                break;
            case ("PAST"):
                for (Booking booking : bookings) {
                    if (booking.getEnd().isBefore(LocalDateTime.now())) {
                        result.add(booking);
                    }
                }
                break;
            case ("FUTURE"):
                for (Booking booking : bookings) {
                    if (booking.getStart().isAfter(LocalDateTime.now())) {
                        result.add(booking);
                    }
                }
                break;
            case ("WAITING"):
                for (Booking booking : bookings) {
                    if (booking.getStatus().equals(BookingStatus.WAITING)) {
                        result.add(booking);
                    }
                }
                break;
            case ("REJECTED"):
                for (Booking booking : bookings) {
                    if (booking.getStatus().equals(BookingStatus.REJECTED)) {
                        result.add(booking);
                    }
                }
                break;
            case ("ALL"):
                result.addAll(bookings);
                break;
            default:
                throw new UnknownStateException(String.format("Unknown state: %s", state));
        }
        return result;
    }
}
