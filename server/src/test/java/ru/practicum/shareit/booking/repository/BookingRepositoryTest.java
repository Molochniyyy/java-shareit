package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;

@DataJpaTest
public class BookingRepositoryTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private BookingRepository bookingRepository;
    private final User booker = new User();
    private final User owner = new User();
    private final Item testItem = new Item();

    @BeforeEach
    public void setUp() {
        owner.setName("owner");
        owner.setEmail("owner@mail.com");
        em.persist(owner);
        booker.setName("booker");
        booker.setEmail("booker@mail.com");
        em.persist(booker);
        testItem.setName("item1");
        testItem.setDescription("New description");
        testItem.setAvailable(true);
        testItem.setOwner(owner);
        em.persist(testItem);
        LocalDateTime minDate = LocalDateTime.now().plusDays(1);
        LocalDateTime maxDate = LocalDateTime.now().plusDays(31);
        for (int i = 0; i < 100; i++) {
            Booking booking = Booking.builder().build();
            booking.setItem(testItem);
            booking.setBooker(booker);
            booking.setStatus(BookingStatus.values()
                    [ThreadLocalRandom.current().nextInt(0, 4)]);
            LocalDateTime randomDate = LocalDateTime.ofEpochSecond(
                    ThreadLocalRandom.current().nextLong(
                            minDate.toEpochSecond(ZoneOffset.ofHours(0)),
                            maxDate.toEpochSecond(ZoneOffset.ofHours(0))
                    ),
                    0,
                    ZoneOffset.ofHours(0)
            );
            booking.setStart(randomDate);
            booking.setEnd(randomDate.plusDays(
                    ThreadLocalRandom.current().nextInt(1, 15)
            ));
            em.persist(booking);
        }
    }

    @Test
    void findAllByBookerTest() {
        Page<Booking> bookings = bookingRepository.findAllByBookerOrderByStartDesc(
                booker, PageRequest.of(0, 20));
        Assertions.assertNotEquals(0, bookings.getTotalElements());
        Assertions.assertTrue(bookings.stream().allMatch(
                (Booking booking) -> booking.getBooker().getId().equals(booker.getId())));
    }

    @Test
    void findAllByBookerAndStatusTest() {
        Page<Booking> bookings = bookingRepository.findAllByBookerAndStatusOrderByStartDesc(
                booker, BookingStatus.WAITING, PageRequest.of(0, 20));
        Assertions.assertNotEquals(0, bookings.getTotalElements());
        Assertions.assertTrue(bookings.stream().allMatch(
                (Booking booking) -> booking.getStatus().equals(BookingStatus.WAITING)
                        && booking.getBooker().getId().equals(booker.getId())));
    }

    @Test
    void findAllByBookerAndEndBeforeTest() {
        LocalDateTime dateTime = LocalDateTime.now().plusDays(15);
        Page<Booking> bookings = bookingRepository.findAllByBookerAndEndBeforeOrderByStartDesc(
                booker,
                dateTime,
                PageRequest.of(0, 20));
        Assertions.assertNotEquals(0, bookings.getTotalElements());
        Assertions.assertTrue(bookings.stream().allMatch(
                (Booking booking) -> booking.getEnd().isBefore(dateTime)
                        && booking.getBooker().getId().equals(booker.getId())));
    }

    @Test
    void findAllByBookerAndStartAfterTest() {
        LocalDateTime dateTime = LocalDateTime.now().plusDays(15);
        Page<Booking> bookings = bookingRepository.findAllByBookerAndStartAfterOrderByStartDesc(
                booker,
                dateTime,
                PageRequest.of(0, 20));
        Assertions.assertNotEquals(0, bookings.getTotalElements());
        Assertions.assertTrue(bookings.stream().allMatch(
                (Booking booking) -> booking.getStart().isAfter(dateTime)
                        && booking.getBooker().getId().equals(booker.getId())));
    }

    @Test
    void findByBookerAndDateTest() {
        LocalDateTime dateTime = LocalDateTime.now().plusDays(15);
        Page<Booking> bookings = bookingRepository.findBookingByBookerAndDate(
                booker,
                dateTime,
                PageRequest.of(0, 20));
        Assertions.assertNotEquals(0, bookings.getTotalElements());
        Assertions.assertTrue(bookings.stream().allMatch(
                (Booking booking) -> booking.getStart().isBefore(dateTime)
                        && booking.getEnd().isAfter(dateTime)
                        && booking.getBooker().getId().equals(booker.getId())));
    }

    @Test
    void findAllByOwnerTest() {
        Page<Booking> bookings = bookingRepository.findAllByItemOwnerIsOrderByStartDesc(
                owner, PageRequest.of(0, 20));
        Assertions.assertNotEquals(0, bookings.getTotalElements());
        Assertions.assertTrue(bookings.stream().allMatch(
                (Booking booking) -> booking.getItem().getOwner().getId().equals(owner.getId())));
    }

    @Test
    void findAllByOwnerAndStatusTest() {
        Page<Booking> bookings = bookingRepository.findAllByItemOwnerIsAndStatusOrderByStartDesc(
                owner, BookingStatus.WAITING, PageRequest.of(0, 20));
        Assertions.assertNotEquals(0, bookings.getTotalElements());
        Assertions.assertTrue(bookings.stream().allMatch(
                (Booking booking) -> booking.getStatus().equals(BookingStatus.WAITING)
                        && booking.getItem().getOwner().getId().equals(owner.getId())));
    }

    @Test
    void findAllByOwnerAndEndBeforeTest() {
        LocalDateTime dateTime = LocalDateTime.now().plusDays(15);
        Page<Booking> bookings = bookingRepository.findAllByItemOwnerIsAndEndBeforeOrderByStartDesc(
                owner,
                dateTime,
                PageRequest.of(0, 20));
        Assertions.assertNotEquals(0, bookings.getTotalElements());
        Assertions.assertTrue(bookings.stream().allMatch(
                (Booking booking) -> booking.getEnd().isBefore(dateTime)
                        && booking.getItem().getOwner().getId().equals(owner.getId())));
    }

    @Test
    void findAllByOwnerAndStartAfterTest() {
        LocalDateTime dateTime = LocalDateTime.now().plusDays(15);
        Page<Booking> bookings = bookingRepository.findAllByItemOwnerIsAndStartAfterOrderByStartDesc(
                owner,
                dateTime,
                PageRequest.of(0, 20));
        Assertions.assertNotEquals(0, bookings.getTotalElements());
        Assertions.assertTrue(bookings.stream().allMatch(
                (Booking booking) -> booking.getStart().isAfter(dateTime)
                        && booking.getItem().getOwner().getId().equals(owner.getId())));
    }

    @Test
    void findByOwnerAndDateTest() {
        LocalDateTime dateTime = LocalDateTime.now().plusDays(15);
        Page<Booking> bookings = bookingRepository.findBookingByOwnerAndDate(
                owner,
                dateTime,
                PageRequest.of(0, 20));
        Assertions.assertNotEquals(0, bookings.getTotalElements());
        Assertions.assertTrue(bookings.stream().allMatch(
                (Booking booking) -> booking.getStart().isBefore(dateTime)
                        && booking.getEnd().isAfter(dateTime)
                        && booking.getItem().getOwner().getId().equals(owner.getId())));
    }

    @Test
    void findByBookerAndItemAndEndBeforeTest() {
        LocalDateTime dateTime = LocalDateTime.now().plusDays(15);
        Collection<Booking> bookings = bookingRepository.findAllByBookerAndItemAndEndBeforeOrderByStartDesc(
                booker,
                testItem,
                dateTime);
        Assertions.assertNotEquals(0, bookings.size());
        Assertions.assertTrue(bookings.stream().allMatch(
                (Booking booking) -> booking.getEnd().isBefore(dateTime)
                        && booking.getItem().getId().equals(testItem.getId())
                        && booking.getBooker().getId().equals(booker.getId())));
    }

    @Test
    void findLastBookingTest() {
        LocalDateTime dateTime = LocalDateTime.now().plusDays(15);
        Booking booking = bookingRepository.findFirstByItemIsAndEndBeforeOrderByEndDesc(
                testItem,
                dateTime).orElse(null);
        Assertions.assertNotNull(booking);
        Booking lastBooking = bookingRepository.findAll().stream()
                .filter((Booking b) -> b.getItem().getId().equals(testItem.getId()))
                .filter((Booking b) -> b.getEnd().isBefore(dateTime))
                .max(Comparator.comparing(Booking::getEnd))
                .orElse(new Booking());
        Assertions.assertEquals(booking.getId(), lastBooking.getId());
    }

    @Test
    void findNextBookingTest() {
        LocalDateTime dateTime = LocalDateTime.now().plusDays(15);
        Booking booking = bookingRepository.findFirstByItemIsAndStartAfterOrderByStartAsc(
                testItem,
                dateTime).orElse(null);
        Assertions.assertNotNull(booking);
        Booking nextBooking = bookingRepository.findAll().stream()
                .filter((Booking b) -> b.getItem().getId().equals(testItem.getId()))
                .filter((Booking b) -> b.getStart().isAfter(dateTime) || b.getStart().isEqual(dateTime))
                .min(Comparator.comparing(Booking::getStart))
                .orElse(new Booking());
        Assertions.assertEquals(booking.getId(), nextBooking.getId());
    }
}
