package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceIntegrationTest {
    private final EntityManager em;
    private final UserService userService;
    private final ItemService itemService;
    private final BookingService bookingService;

    @Test
    void getBookingsOfUserItemsTest() {
        UserDto owner = userService.saveUser(new UserDto(null, "owner", "owner@mail.com"));
        UserDto bookerDto = userService.saveUser(new UserDto(null, "booker", "booker@mail.com"));
        ItemDto item = itemService.add(owner.getId(), new ItemDto(
                null,
                "item1",
                "description",
                true,
                null,
                null,
                null,
                null
        ));
       bookingService.addBooking(bookerDto.getId(),
                new BookingDtoRequest(
                        item.getId(),
                        LocalDateTime.now().plusHours(5),
                        LocalDateTime.now().plusHours(8)
                ));
        bookingService.addBooking(bookerDto.getId(),
                new BookingDtoRequest(
                        item.getId(),
                        LocalDateTime.now().plusHours(15),
                        LocalDateTime.now().plusHours(18)
                ));
        bookingService.addBooking(bookerDto.getId(),
                new BookingDtoRequest(
                        item.getId(),
                        LocalDateTime.now().plusHours(1),
                        LocalDateTime.now().plusHours(2)
                ));
        BookingDto rejectedBooking = bookingService.addBooking(bookerDto.getId(),
                new BookingDtoRequest(
                        item.getId(),
                        LocalDateTime.of(2023, 3, 10, 12, 0),
                        LocalDateTime.of(2023, 3, 10, 18, 0)
                ));
        bookingService.updateBooking(owner.getId(), rejectedBooking.getId(), false);
        User booker = em.createQuery("Select u from User u where u.id = :id", User.class)
                .setParameter("id", bookerDto.getId())
                .getSingleResult();

        Collection<BookingDto> allBookings = bookingService.getBookingsByBookerId(booker.getId(),
                BookingState.ALL,
                0,
                20);
        Collection<Booking> expectedAll = em.createQuery("Select b from Booking b where b.booker = :booker",
                        Booking.class)
                .setParameter("booker", booker)
                .getResultList();
        assertThat(allBookings, notNullValue());
        assertThat(allBookings.size(), equalTo(expectedAll.size()));
        assertThat(resultContainsAllExpectedIds(allBookings, expectedAll), equalTo(true));

        Collection<BookingDto> currentBookings = bookingService.getBookingsByBookerId(booker.getId(),
                BookingState.CURRENT,
                0,
                20);
        Collection<Booking> expectedCurrent = em.createQuery("Select b from Booking b where b.booker = :booker " +
                                "and b.start < :now and b.end > :now",
                        Booking.class)
                .setParameter("booker", booker)
                .setParameter("now", LocalDateTime.now())
                .getResultList();
        assertThat(currentBookings, notNullValue());
        assertThat(currentBookings.size(), equalTo(expectedCurrent.size()));
        assertThat(resultContainsAllExpectedIds(currentBookings, expectedCurrent), equalTo(true));

        Collection<BookingDto> pastBookings = bookingService.getBookingsByBookerId(booker.getId(),
                BookingState.PAST,
                0,
                20);
        Collection<Booking> expectedPast = em.createQuery("Select b from Booking b where b.booker = :booker " +
                                "and b.end < :now",
                        Booking.class)
                .setParameter("booker", booker)
                .setParameter("now", LocalDateTime.now())
                .getResultList();
        assertThat(pastBookings, notNullValue());
        assertThat(pastBookings.size(), equalTo(expectedPast.size()));
        assertThat(resultContainsAllExpectedIds(pastBookings, expectedPast), equalTo(true));

        Collection<BookingDto> futureBookings = bookingService.getBookingsByBookerId(booker.getId(),
                BookingState.FUTURE,
                0,
                20);
        Collection<Booking> expectedFuture = em.createQuery("Select b from Booking b where b.booker = :booker " +
                                "and b.start > :now",
                        Booking.class)
                .setParameter("booker", booker)
                .setParameter("now", LocalDateTime.now())
                .getResultList();
        assertThat(futureBookings, notNullValue());
        assertThat(futureBookings.size(), equalTo(expectedFuture.size()));
        assertThat(resultContainsAllExpectedIds(futureBookings, expectedFuture), equalTo(true));

        Collection<BookingDto> waitingBookings = bookingService.getBookingsByBookerId(booker.getId(),
                BookingState.WAITING,
                0,
                20);
        Collection<Booking> expectedWaiting = em.createQuery("Select b from Booking b where b.booker = :booker " +
                                "and b.status = :status",
                        Booking.class)
                .setParameter("booker", booker)
                .setParameter("status", BookingStatus.WAITING)
                .getResultList();
        assertThat(waitingBookings, notNullValue());
        assertThat(waitingBookings.size(), equalTo(expectedWaiting.size()));
        assertThat(resultContainsAllExpectedIds(waitingBookings, expectedWaiting), equalTo(true));

        Collection<BookingDto> rejectedBookings = bookingService.getBookingsByBookerId(booker.getId(),
                BookingState.REJECTED,
                0,
                20);
        Collection<Booking> expectedRejected = em.createQuery("Select b from Booking b where b.booker = :booker " +
                                "and b.status = :status",
                        Booking.class)
                .setParameter("booker", booker)
                .setParameter("status", BookingStatus.REJECTED)
                .getResultList();
        assertThat(rejectedBookings, notNullValue());
        assertThat(rejectedBookings.size(), equalTo(expectedRejected.size()));
        assertThat(resultContainsAllExpectedIds(rejectedBookings, expectedRejected), equalTo(true));
    }

    private boolean resultContainsAllExpectedIds(Collection<BookingDto> result, Collection<Booking> expected) {
        Collection<Long> expectedIds = expected.stream()
                .map(Booking::getId)
                .collect(Collectors.toList());
        return result.stream().map(BookingDto::getId).collect(Collectors.toList())
                .containsAll(expectedIds);
    }
}