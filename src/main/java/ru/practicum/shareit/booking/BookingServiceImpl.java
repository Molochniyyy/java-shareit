package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService{

    private final BookingRepository repository;
    private final BookingRepositoryImpl repositoryImpl;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDto addBooking(Long userId, Booking booking) {

        return null;
    }

    @Override
    public BookingDto updateBooking(Long userId, Long bookingId, String approved) {
        return null;
    }

    @Override
    public BookingDto getBookingById(Long userId, Long bookingId) {
        return null;
    }

    @Override
    public List<BookingDto> getBookingsByBookerId(Long userId, String state) {
        return null;
    }

    @Override
    public List<BookingDto> getBookingAllItemsByOwnerId(Long userId, String all) {
        return null;
    }
}
