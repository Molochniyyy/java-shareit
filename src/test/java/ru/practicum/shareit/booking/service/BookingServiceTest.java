package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.UnavailableItemException;
import ru.practicum.shareit.exceptions.UnsupportedOperationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

public class BookingServiceTest {
    @Test
    void createBookingOwnerTest() {
        BookingRepository bookingRepository = Mockito.mock(BookingRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
        BookingService bookingService = new BookingServiceImpl(bookingRepository,
                userRepository,
                itemRepository);
        User testUser = new User(1L, "testname", "test@mail.com");
        Item testItem = new Item(1L, "test", "description", true, testUser, null);
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(testItem));
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(testUser));
        Assertions.assertThrows(ObjectNotFoundException.class, () -> bookingService.addBooking(1L,
                new BookingDtoRequest(1L, LocalDateTime.now(), LocalDateTime.now().plusHours(5))));
        Mockito
                .verify(userRepository, Mockito.times(1))
                .findById(anyLong());
        Mockito
                .verify(itemRepository, Mockito.times(1))
                .findById(anyLong());
        Mockito.verifyNoMoreInteractions(userRepository, itemRepository, bookingRepository);
    }

    @Test
    void createBookingItemNotAvailableTest() {
        BookingRepository bookingRepository = Mockito.mock(BookingRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
        BookingService bookingService = new BookingServiceImpl(bookingRepository,
                userRepository,
                itemRepository);
        User testUser = new User(1L, "testname", "test@mail.com");
        Item testItem = new Item(1L,
                "test",
                "description",
                false,
                new User(2L, "test", "t@mail.com"),
                null);
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(testItem));
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(testUser));
        Assertions.assertThrows(UnavailableItemException.class, () -> bookingService.addBooking(1L,
                new BookingDtoRequest(1L, LocalDateTime.now(), LocalDateTime.now().plusHours(5))));
        Mockito
                .verify(userRepository, Mockito.times(1))
                .findById(anyLong());
        Mockito
                .verify(itemRepository, Mockito.times(1))
                .findById(anyLong());
        Mockito.verifyNoMoreInteractions(userRepository, itemRepository, bookingRepository);
    }

    @Test
    void createBookingTest() {
        BookingRepository bookingRepository = Mockito.mock(BookingRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
        BookingService bookingService = new BookingServiceImpl(bookingRepository,
                userRepository,
                itemRepository);
        User testUser = new User(1L, "testname", "test@mail.com");
        Item testItem = new Item(1L,
                "test",
                "description",
                true,
                new User(2L, "test", "t@mail.com"),
                null);
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(testItem));
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(testUser));
        Mockito
                .when(bookingRepository.save(any(Booking.class)))
                .thenReturn(new Booking(1L,
                        testItem,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(5),
                        testUser,
                        BookingStatus.WAITING));
        bookingService.addBooking(1L,
                new BookingDtoRequest( 1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(5)));
        Mockito
                .verify(userRepository, Mockito.times(1))
                .findById(anyLong());
        Mockito
                .verify(itemRepository, Mockito.times(1))
                .findById(anyLong());
        Mockito
                .verify(bookingRepository, Mockito.times(1))
                .save(any(Booking.class));
        Mockito.verifyNoMoreInteractions(userRepository, itemRepository, bookingRepository);
    }

    @Test
    void patchBookingStatusWrongUserTest() {
        BookingRepository bookingRepository = Mockito.mock(BookingRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
        BookingService bookingService = new BookingServiceImpl(bookingRepository,
                userRepository,
                itemRepository);
        User testUser = new User(1L, "testname", "test@mail.com");
        Item testItem = new Item(1L,
                "test",
                "description",
                true,
                new User(2L, "test", "t@mail.com"),
                null);
        Booking testBooking = new Booking(1L,
                testItem,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(5),
                testUser,
                BookingStatus.WAITING);
        Mockito
                .when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(testBooking));
        Assertions.assertThrows(ObjectNotFoundException.class,
                () -> bookingService.updateBooking(1L, 1L, true));
        Mockito
                .verify(bookingRepository, Mockito.times(1))
                .findById(anyLong());
        Mockito.verifyNoMoreInteractions(userRepository, itemRepository, bookingRepository);
    }

    @Test
    void patchBookingStatusWrongStatusTest() {
        BookingRepository bookingRepository = Mockito.mock(BookingRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
        BookingService bookingService = new BookingServiceImpl(bookingRepository,
                userRepository,
                itemRepository);
        User testUser = new User(1L, "testname", "test@mail.com");
        Item testItem = new Item(1L,
                "test",
                "description",
                true,
                new User(1L, "test", "t@mail.com"),
                null);
        Booking testBooking = new Booking(1L,
                testItem,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(5),
                testUser,
                BookingStatus.REJECTED);
        Mockito
                .when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(testBooking));
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> bookingService.updateBooking(1L, 1L, true));
        Mockito
                .verify(bookingRepository, Mockito.times(1))
                .findById(anyLong());
        Mockito.verifyNoMoreInteractions(userRepository, itemRepository, bookingRepository);
    }

    @Test
    void patchBookingStatusApprovedTest() {
        BookingRepository bookingRepository = Mockito.mock(BookingRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
        BookingService bookingService = new BookingServiceImpl(bookingRepository,
                userRepository,
                itemRepository);
        User testUser = new User(1L, "testname", "test@mail.com");
        Item testItem = new Item(1L,
                "test",
                "description",
                true,
                new User(1L, "test", "t@mail.com"),
                null);
        Booking testBooking = new Booking(1L,
                testItem,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(5),
                testUser,
                BookingStatus.WAITING);
        Mockito
                .when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(testBooking));
        Mockito
                .when(bookingRepository.save(any(Booking.class)))
                .then(org.mockito.AdditionalAnswers.returnsFirstArg());
        BookingDto answer = bookingService.updateBooking(1L, 1L, true);
        Assertions.assertEquals(answer.getStatus(), BookingStatus.APPROVED);
        Mockito
                .verify(bookingRepository, Mockito.times(1))
                .findById(anyLong());
        Mockito
                .verify(bookingRepository, Mockito.times(1))
                .save(any(Booking.class));
        Mockito.verifyNoMoreInteractions(userRepository, itemRepository, bookingRepository);
    }

    @Test
    void patchBookingStatusRejectedTest() {
        BookingRepository bookingRepository = Mockito.mock(BookingRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
        BookingService bookingService = new BookingServiceImpl(bookingRepository,
                userRepository,
                itemRepository);
        User testUser = new User(1L, "testname", "test@mail.com");
        Item testItem = new Item(1L,
                "test",
                "description",
                true,
                new User(1L, "test", "t@mail.com"),
                null);
        Booking testBooking = new Booking(1L,
                testItem,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(5),
                testUser,
                BookingStatus.WAITING);
        Mockito
                .when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(testBooking));
        Mockito
                .when(bookingRepository.save(any(Booking.class)))
                .then(org.mockito.AdditionalAnswers.returnsFirstArg());
        BookingDto answer = bookingService.updateBooking(1L, 1L, false);
        Assertions.assertEquals(answer.getStatus(), BookingStatus.REJECTED);
        Mockito
                .verify(bookingRepository, Mockito.times(1))
                .findById(anyLong());
        Mockito
                .verify(bookingRepository, Mockito.times(1))
                .save(any(Booking.class));
        Mockito.verifyNoMoreInteractions(userRepository, itemRepository, bookingRepository);
    }

    @Test
    void getBookingWrongUserTest() {
        BookingRepository bookingRepository = Mockito.mock(BookingRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
        BookingService bookingService = new BookingServiceImpl(bookingRepository,
                userRepository,
                itemRepository);
        User testOwner = new User(1L, "test name", "test@mail.com");
        User testBooker = new User(2L, "test booker", "booker@mail.com");
        Item testItem = new Item(1L,
                "test",
                "description",
                true,
                testOwner,
                null);
        Booking testBooking = new Booking(1L,
                testItem,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(5),
                testBooker,
                BookingStatus.WAITING);
        Mockito
                .when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(testBooking));
        Assertions.assertThrows(ObjectNotFoundException.class,
                () -> bookingService.getBookingById(3L, 1L));
        Mockito
                .verify(bookingRepository, Mockito.times(1))
                .findById(anyLong());
    }

    @Test
    void getBookingOwnerOrBookerTest() {
        BookingRepository bookingRepository = Mockito.mock(BookingRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
        BookingService bookingService = new BookingServiceImpl(bookingRepository,
                userRepository,
                itemRepository);
        User testOwner = new User(1L, "test name", "test@mail.com");
        User testBooker = new User(2L, "test booker", "booker@mail.com");
        Item testItem = new Item(1L,
                "test",
                "description",
                true,
                testOwner,
                null);
        Booking testBooking = new Booking(1L,
                testItem,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(5),
                testBooker,
                BookingStatus.WAITING);
        Mockito
                .when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(testBooking));
        Assertions.assertDoesNotThrow(() -> bookingService.getBookingById(testOwner.getId(), 1L));
        Assertions.assertDoesNotThrow(() -> bookingService.getBookingById(testBooker.getId(), 1L));
        Mockito
                .verify(bookingRepository, Mockito.times(2))
                .findById(anyLong());
        Mockito.verifyNoMoreInteractions(userRepository, itemRepository, bookingRepository);
    }

    @Test
    void getBookingsOfUserTest() {
        BookingRepository bookingRepository = Mockito.mock(BookingRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
        BookingService bookingService = new BookingServiceImpl(bookingRepository,
                userRepository,
                itemRepository);
        User testOwner = new User(1L, "test name", "test@mail.com");
        User testBooker = new User(2L, "test booker", "booker@mail.com");
        Item testItem = new Item(1L,
                "test",
                "description",
                true,
                testOwner,
                null);
        Booking testBooking = new Booking(1L,
                testItem,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(5),
                testBooker,
                BookingStatus.WAITING);
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(testOwner));
        Mockito
                .when(bookingRepository.findAllByBookerOrderByStartDesc(any(User.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(testBooking)));
        Mockito
                .when(bookingRepository.findBookingByBookerAndDate(any(User.class),
                        any(LocalDateTime.class),
                        any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(testBooking)));
        Mockito
                .when(bookingRepository.findAllByBookerAndEndBeforeOrderByStartDesc(any(User.class),
                        any(LocalDateTime.class),
                        any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(testBooking)));
        Mockito
                .when(bookingRepository.findAllByBookerAndStartAfterOrderByStartDesc(any(User.class),
                        any(LocalDateTime.class),
                        any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(testBooking)));
        Mockito
                .when(bookingRepository.findAllByBookerAndStatusOrderByStartDesc(any(User.class),
                        any(BookingStatus.class),
                        any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(testBooking)));
        bookingService.getBookingsByBookerId(2L, BookingState.ALL, 0, 20);
        bookingService.getBookingsByBookerId(2L, BookingState.CURRENT, 0, 20);
        bookingService.getBookingsByBookerId(2L, BookingState.PAST, 0, 20);
        bookingService.getBookingsByBookerId(2L, BookingState.FUTURE, 0, 20);
        bookingService.getBookingsByBookerId(2L, BookingState.WAITING, 0, 20);
        bookingService.getBookingsByBookerId(2L, BookingState.REJECTED, 0, 20);
        Mockito
                .verify(userRepository, Mockito.times(6))
                .findById(anyLong());
        Mockito
                .verify(bookingRepository, Mockito.times(1))
                .findAllByBookerOrderByStartDesc(any(User.class), any(Pageable.class));
        Mockito
                .verify(bookingRepository, Mockito.times(1))
                .findBookingByBookerAndDate(any(User.class), any(LocalDateTime.class), any(Pageable.class));
        Mockito
                .verify(bookingRepository, Mockito.times(1))
                .findAllByBookerAndEndBeforeOrderByStartDesc(any(User.class),
                        any(LocalDateTime.class),
                        any(Pageable.class));
        Mockito
                .verify(bookingRepository, Mockito.times(1))
                .findAllByBookerAndStartAfterOrderByStartDesc(any(User.class),
                        any(LocalDateTime.class),
                        any(Pageable.class));
        Mockito
                .verify(bookingRepository, Mockito.times(2))
                .findAllByBookerAndStatusOrderByStartDesc(any(User.class),
                        any(BookingStatus.class),
                        any(Pageable.class));
        Mockito.verifyNoMoreInteractions(userRepository, itemRepository, bookingRepository);
    }

    @Test
    void getBookingsOfUserItemsTest() {
        BookingRepository bookingRepository = Mockito.mock(BookingRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
        BookingService bookingService = new BookingServiceImpl(bookingRepository,
                userRepository,
                itemRepository);
        User testOwner = new User(1L, "test name", "test@mail.com");
        User testBooker = new User(2L, "test booker", "booker@mail.com");
        Item testItem = new Item(1L,
                "test",
                "description",
                true,
                testOwner,
                null);
        Booking testBooking = new Booking(1L,
                testItem,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(5),
                testBooker,
                BookingStatus.WAITING);
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(testOwner));
        Mockito
                .when(bookingRepository.findAllByItemOwnerIsOrderByStartDesc(any(User.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(testBooking)));
        Mockito
                .when(bookingRepository.findBookingByOwnerAndDate(any(User.class),
                        any(LocalDateTime.class),
                        any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(testBooking)));
        Mockito
                .when(bookingRepository.findAllByItemOwnerIsAndEndBeforeOrderByStartDesc(any(User.class),
                        any(LocalDateTime.class),
                        any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(testBooking)));
        Mockito
                .when(bookingRepository.findAllByItemOwnerIsAndStartAfterOrderByStartDesc(any(User.class),
                        any(LocalDateTime.class),
                        any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(testBooking)));
        Mockito
                .when(bookingRepository.findAllByItemOwnerIsAndStatusOrderByStartDesc(any(User.class),
                        any(BookingStatus.class),
                        any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(testBooking)));
        bookingService.getBookingAllItemsByOwnerId(1L, BookingState.ALL, 0, 20);
        bookingService.getBookingAllItemsByOwnerId(1L, BookingState.CURRENT, 0, 20);
        bookingService.getBookingAllItemsByOwnerId(1L, BookingState.PAST, 0, 20);
        bookingService.getBookingAllItemsByOwnerId(1L, BookingState.FUTURE, 0, 20);
        bookingService.getBookingAllItemsByOwnerId(1L, BookingState.WAITING, 0, 20);
        bookingService.getBookingAllItemsByOwnerId(1L, BookingState.REJECTED, 0, 20);
        Mockito
                .verify(userRepository, Mockito.times(6))
                .findById(anyLong());
        Mockito
                .verify(bookingRepository, Mockito.times(1))
                .findAllByItemOwnerIsOrderByStartDesc(any(User.class), any(Pageable.class));
        Mockito
                .verify(bookingRepository, Mockito.times(1))
                .findBookingByOwnerAndDate(any(User.class), any(LocalDateTime.class), any(Pageable.class));
        Mockito
                .verify(bookingRepository, Mockito.times(1))
                .findAllByItemOwnerIsAndEndBeforeOrderByStartDesc(any(User.class),
                        any(LocalDateTime.class),
                        any(Pageable.class));
        Mockito
                .verify(bookingRepository, Mockito.times(1))
                .findAllByItemOwnerIsAndStartAfterOrderByStartDesc(any(User.class),
                        any(LocalDateTime.class),
                        any(Pageable.class));
        Mockito
                .verify(bookingRepository, Mockito.times(2))
                .findAllByItemOwnerIsAndStatusOrderByStartDesc(any(User.class),
                        any(BookingStatus.class),
                        any(Pageable.class));
        Mockito.verifyNoMoreInteractions(userRepository, itemRepository, bookingRepository);
    }
}