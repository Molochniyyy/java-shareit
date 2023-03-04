package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.UnauthorizedAccessException;
import ru.practicum.shareit.exceptions.UnsupportedOperationException;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;

public class ItemServiceTest {
    @Test
    void addItemTest() {
        ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        CommentRepository commentRepository = Mockito.mock(CommentRepository.class);
        BookingRepository bookingRepository = Mockito.mock(BookingRepository.class);
        ItemRequestRepository itemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        ItemService itemService = new ItemServiceImpl(itemRepository,
                bookingRepository,
                commentRepository,
                userRepository,
                itemRequestRepository);
        User testUser = new User(1L, "testname", "test@mail.com");
        Item testItem = new Item(1L, "test", "description", true, testUser, null);

        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(testUser));
        Mockito
                .when(itemRepository.save(any(Item.class)))
                .thenReturn(testItem);

        itemService.add(1L, new ItemDto(1L,
                "test",
                "description",
                true,
                null,
                null,
                null,
                null));

        Mockito
                .verify(userRepository, Mockito.times(1))
                .findById(anyLong());
        Mockito
                .verify(itemRepository, Mockito.times(1))
                .save(any(Item.class));
        Mockito.verifyNoMoreInteractions(userRepository, itemRepository, commentRepository, bookingRepository, itemRequestRepository);
    }

    @Test
    void addItemWithRequestTest() {
        ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        CommentRepository commentRepository = Mockito.mock(CommentRepository.class);
        BookingRepository bookingRepository = Mockito.mock(BookingRepository.class);
        ItemRequestRepository itemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        ItemService itemService = new ItemServiceImpl(itemRepository,
                bookingRepository,
                commentRepository,
                userRepository,
                itemRequestRepository);
        User testUser = new User(1L, "testname", "test@mail.com");
        Item testItem = new Item(1L, "test", "description", true, testUser, null);

        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(testUser));
        Mockito
                .when(itemRepository.save(any(Item.class)))
                .thenReturn(testItem);
        Mockito
                .when(itemRequestRepository.findById(1L))
                .thenReturn(Optional.of(new ItemRequest()));

        itemService.add(1L, new ItemDto(1L,
                "test",
                "description",
                true,
                1L,
                null,
                null,
                null));

        Mockito
                .verify(userRepository, Mockito.times(1))
                .findById(anyLong());
        Mockito
                .verify(itemRequestRepository, Mockito.times(1))
                .findById(anyLong());
        Mockito
                .verify(itemRepository, Mockito.times(1))
                .save(any(Item.class));
        Mockito.verifyNoMoreInteractions(userRepository, itemRepository, commentRepository, bookingRepository, itemRequestRepository);
    }

    @Test
    void getItemsTest() {
        ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        CommentRepository commentRepository = Mockito.mock(CommentRepository.class);
        BookingRepository bookingRepository = Mockito.mock(BookingRepository.class);
        ItemRequestRepository itemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        ItemService itemService = new ItemServiceImpl(itemRepository,
                bookingRepository,
                commentRepository,
                userRepository,
                itemRequestRepository);
        User testUser = new User(1L, "testname", "test@mail.com");
        Item testItem = new Item(1L, "test", "description", true, testUser, null);

        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(testUser));
        Mockito
                .when(itemRepository.findAllByOwnerIsOrderByIdAsc(any(User.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(testItem)));
        Mockito
                .when(bookingRepository.findFirstByItemIsAndEndBeforeOrderByEndDesc(any(Item.class), any(LocalDateTime.class)))
                .thenReturn(Optional.empty());
        Mockito
                .when(bookingRepository.findFirstByItemIsAndStartAfterOrderByStartAsc(any(Item.class), any(LocalDateTime.class)))
                .thenReturn(Optional.empty());
        Mockito
                .when(commentRepository.findByItemIsOrderByCreatedDesc(any(Item.class)))
                .thenReturn(List.of());

        itemService.getItems(1L, 0, 20);

        Mockito
                .verify(userRepository, Mockito.times(1))
                .findById(anyLong());
        Mockito
                .verify(itemRepository, Mockito.times(1))
                .findAllByOwnerIsOrderByIdAsc(any(User.class), any(Pageable.class));
    }

    @Test
    void patchItemTest() {
        ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        CommentRepository commentRepository = Mockito.mock(CommentRepository.class);
        BookingRepository bookingRepository = Mockito.mock(BookingRepository.class);
        ItemRequestRepository itemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        ItemService itemService = new ItemServiceImpl(itemRepository,
                bookingRepository,
                commentRepository,
                userRepository,
                itemRequestRepository);
        User testUser = new User(1L, "testname", "test@mail.com");
        Item testItem = new Item(1L, "test", "description", true, testUser, null);

        Mockito
                .when(itemRepository.findById(1L))
                .thenReturn(Optional.of(testItem));
        Mockito
                .when(itemRepository.save(any(Item.class)))
                .then(org.mockito.AdditionalAnswers.returnsFirstArg());
        Mockito
                .when(commentRepository.findByItemIsOrderByCreatedDesc(any(Item.class)))
                .thenReturn(List.of());

        Assertions.assertThrows(UnauthorizedAccessException.class, () -> itemService.update(
                2L,
                1L,
                new ItemDto(null,
                        "updated name",
                        null,
                        null,
                        null,
                        null,
                        null,
                        null)));

        ItemDto answer = itemService.update(1L, 1L, new ItemDto(null,
                "updated name",
                null,
                null,
                null,
                null,
                null,
                null));
        Assertions.assertEquals(answer.getName(), "updated name");
        Assertions.assertEquals(answer.getDescription(), testItem.getDescription());
        Assertions.assertEquals(answer.getAvailable(), testItem.getAvailable());

        answer = itemService.update(1L, 1L, new ItemDto(null,
                null,
                "updated description",
                null,
                null,
                null,
                null,
                null));
        Assertions.assertEquals(answer.getName(), testItem.getName());
        Assertions.assertEquals(answer.getDescription(), "updated description");
        Assertions.assertEquals(answer.getAvailable(), testItem.getAvailable());

        answer = itemService.update(1L, 1L, new ItemDto(null,
                null,
                null,
                false,
                null,
                null,
                null,
                null));
        Assertions.assertEquals(answer.getName(), testItem.getName());
        Assertions.assertEquals(answer.getDescription(), testItem.getDescription());
        Assertions.assertEquals(answer.getAvailable(), false);

        answer = itemService.update(1L, 1L, new ItemDto(null,
                "updated name",
                "updated description",
                false,
                null,
                null,
                null,
                null));
        Assertions.assertEquals(answer.getName(), "updated name");
        Assertions.assertEquals(answer.getDescription(), "updated description");
        Assertions.assertEquals(answer.getAvailable(), false);

        Mockito
                .verify(itemRepository, Mockito.times(5))
                .findById(anyLong());
        Mockito
                .verify(itemRepository, Mockito.times(4))
                .save(any(Item.class));
        Mockito
                .verify(commentRepository, Mockito.times(4))
                .findByItemIsOrderByCreatedDesc(any(Item.class));
        Mockito.verifyNoMoreInteractions(userRepository, itemRepository, commentRepository, bookingRepository, itemRequestRepository);
    }

    @Test
    void getItemOtherUserTest() {
        ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        CommentRepository commentRepository = Mockito.mock(CommentRepository.class);
        BookingRepository bookingRepository = Mockito.mock(BookingRepository.class);
        ItemRequestRepository itemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        ItemService itemService = new ItemServiceImpl(itemRepository,
                bookingRepository,
                commentRepository,
                userRepository,
                itemRequestRepository);
        User testUser = new User(1L, "testname", "test@mail.com");
        Item testItem = new Item(1L, "test", "description", true, testUser, null);

        Mockito
                .when(itemRepository.findById(1L))
                .thenReturn(Optional.of(testItem));
        Mockito
                .when(bookingRepository.findFirstByItemIsAndEndBeforeOrderByEndDesc(any(Item.class), any(LocalDateTime.class)))
                .thenReturn(Optional.of(new Booking(
                        testItem,
                        LocalDateTime.of(2000, 1, 21, 12, 30),
                        LocalDateTime.of(2022, 1, 21, 12, 30),
                        testUser,
                        BookingStatus.APPROVED)));
        Mockito
                .when(bookingRepository.findFirstByItemIsAndStartAfterOrderByStartAsc(any(Item.class), any(LocalDateTime.class)))
                .thenReturn(Optional.of(new Booking(
                        testItem,
                        LocalDateTime.of(2023, 3, 21, 12, 30),
                        LocalDateTime.of(2023, 5, 1, 12, 30),
                        testUser,
                        BookingStatus.APPROVED)));
        Mockito
                .when(commentRepository.findByItemIsOrderByCreatedDesc(any(Item.class)))
                .thenReturn(List.of());

        ItemDto answer = itemService.getById(2L, 1L);
        Assertions.assertNull(answer.getNextBooking());
        Assertions.assertNull(answer.getLastBooking());
        Assertions.assertNotNull(answer.getComments());

        Mockito
                .verify(itemRepository, Mockito.times(1))
                .findById(anyLong());
        Mockito
                .verify(commentRepository, Mockito.times(1))
                .findByItemIsOrderByCreatedDesc(any(Item.class));
        Mockito.verifyNoMoreInteractions(userRepository, itemRepository, commentRepository, bookingRepository, itemRequestRepository);
    }

    @Test
    void getItemForOwnerTest() {
        ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        CommentRepository commentRepository = Mockito.mock(CommentRepository.class);
        BookingRepository bookingRepository = Mockito.mock(BookingRepository.class);
        ItemRequestRepository itemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        ItemService itemService = new ItemServiceImpl(itemRepository,
                bookingRepository,
                commentRepository,
                userRepository,
                itemRequestRepository);
        User testUser = new User(1L, "testname", "test@mail.com");
        Item testItem = new Item(1L, "test", "description", true, testUser, null);

        Mockito
                .when(itemRepository.findById(1L))
                .thenReturn(Optional.of(testItem));
        Mockito
                .when(bookingRepository.findFirstByItemIsAndEndBeforeOrderByEndDesc(any(Item.class), any(LocalDateTime.class)))
                .thenReturn(Optional.of(new Booking(
                        testItem,
                        LocalDateTime.of(2000, 1, 21, 12, 30),
                        LocalDateTime.of(2022, 1, 21, 12, 30),
                        testUser,
                        BookingStatus.APPROVED)));
        Mockito
                .when(bookingRepository.findFirstByItemIsAndStartAfterOrderByStartAsc(any(Item.class), any(LocalDateTime.class)))
                .thenReturn(Optional.of(new Booking(
                        testItem,
                        LocalDateTime.of(2023, 3, 21, 12, 30),
                        LocalDateTime.of(2023, 5, 1, 12, 30),
                        testUser,
                        BookingStatus.APPROVED)));
        Mockito
                .when(commentRepository.findByItemIsOrderByCreatedDesc(any(Item.class)))
                .thenReturn(List.of());

        ItemDto answer = itemService.getById(1L, 1L);
        Assertions.assertNotNull(answer.getNextBooking());
        Assertions.assertNotNull(answer.getLastBooking());
        Assertions.assertNotNull(answer.getComments());

        Mockito
                .verify(itemRepository, Mockito.times(1))
                .findById(anyLong());
        Mockito
                .verify(bookingRepository, Mockito.times(1))
                .findFirstByItemIsAndEndBeforeOrderByEndDesc(any(Item.class), any(LocalDateTime.class));
        Mockito
                .verify(bookingRepository, Mockito.times(1))
                .findFirstByItemIsAndStartAfterOrderByStartAsc(any(Item.class), any(LocalDateTime.class));
        Mockito
                .verify(commentRepository, Mockito.times(1))
                .findByItemIsOrderByCreatedDesc(any(Item.class));
        Mockito.verifyNoMoreInteractions(userRepository, itemRepository, commentRepository, bookingRepository, itemRequestRepository);
    }

    @Test
    void searchItemsBlankRequestTest() {
        ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        CommentRepository commentRepository = Mockito.mock(CommentRepository.class);
        BookingRepository bookingRepository = Mockito.mock(BookingRepository.class);
        ItemRequestRepository itemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        ItemService itemService = new ItemServiceImpl(itemRepository,
                bookingRepository,
                commentRepository,
                userRepository,
                itemRequestRepository);
        User testUser = new User(1L, "testname", "test@mail.com");
        Item testItem = new Item(1L, "test", "description", true, testUser, null);

        Mockito
                .when(itemRepository.searchItems(anyString(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(testItem)));

        Collection<ItemDto> dtos = itemService.searchItems("   ", 0, 20);

        Assertions.assertEquals(dtos.size(), 0);

        Mockito.verifyNoMoreInteractions(userRepository, itemRepository, commentRepository, bookingRepository, itemRequestRepository);
    }

    @Test
    void searchItemsNotBlankRequestTest() {
        ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        CommentRepository commentRepository = Mockito.mock(CommentRepository.class);
        BookingRepository bookingRepository = Mockito.mock(BookingRepository.class);
        ItemRequestRepository itemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        ItemService itemService = new ItemServiceImpl(itemRepository,
                bookingRepository,
                commentRepository,
                userRepository,
                itemRequestRepository);
        User testUser = new User(1L, "testname", "test@mail.com");
        Item testItem = new Item(1L, "test", "description", true, testUser, null);

        Mockito
                .when(itemRepository.searchItems(anyString(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(testItem)));

        Collection<ItemDto> dtos = itemService.searchItems("23", 0, 20);

        Assertions.assertEquals(dtos.size(), 1);

        Mockito
                .verify(itemRepository, Mockito.times(1))
                .searchItems(anyString(), any(Pageable.class));
        Mockito.verifyNoMoreInteractions(userRepository, itemRepository, commentRepository, bookingRepository, itemRequestRepository);
    }

    @Test
    void addCommentNotBookedTest() {
        ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        CommentRepository commentRepository = Mockito.mock(CommentRepository.class);
        BookingRepository bookingRepository = Mockito.mock(BookingRepository.class);
        ItemRequestRepository itemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        ItemService itemService = new ItemServiceImpl(itemRepository,
                bookingRepository,
                commentRepository,
                userRepository,
                itemRequestRepository);
        User testUser = new User(1L, "testname", "test@mail.com");
        Item testItem = new Item(1L, "test", "description", true, testUser, null);

        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(testUser));
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(testItem));
        Mockito
                .when(bookingRepository.findAllByBookerAndItemAndEndBeforeOrderByStartDesc(any(User.class),
                        any(Item.class),
                        any(LocalDateTime.class)))
                .thenReturn(List.of());
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> itemService.addComment(1L, 1L, new CommentRequestDto("comment")));

        Mockito
                .verify(userRepository, Mockito.times(1))
                .findById(anyLong());
        Mockito
                .verify(itemRepository, Mockito.times(1))
                .findById(anyLong());
        Mockito
                .verify(bookingRepository, Mockito.times(1))
                .findAllByBookerAndItemAndEndBeforeOrderByStartDesc(any(User.class),
                        any(Item.class),
                        any(LocalDateTime.class));
        Mockito.verifyNoMoreInteractions(userRepository, itemRepository, commentRepository, bookingRepository, itemRequestRepository);
    }

    @Test
    void addCommentTest() {
        ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        CommentRepository commentRepository = Mockito.mock(CommentRepository.class);
        BookingRepository bookingRepository = Mockito.mock(BookingRepository.class);
        ItemRequestRepository itemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        ItemService itemService = new ItemServiceImpl(itemRepository,
                bookingRepository,
                commentRepository,
                userRepository,
                itemRequestRepository);
        User testUser = new User(1L, "testname", "test@mail.com");
        Item testItem = new Item(1L, "test", "description", true, testUser, null);

        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(testUser));
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(testItem));
        Mockito
                .when(bookingRepository.findAllByBookerAndItemAndEndBeforeOrderByStartDesc(any(User.class),
                        any(Item.class),
                        any(LocalDateTime.class)))
                .thenReturn(List.of(new Booking(
                        testItem,
                        LocalDateTime.of(2000, 1, 21, 12, 30),
                        LocalDateTime.of(2022, 1, 21, 12, 30),
                        testUser,
                        BookingStatus.APPROVED)));
        Mockito
                .when(commentRepository.save(any(Comment.class)))
                .thenReturn(new Comment(1L, testItem, testUser, "text", LocalDateTime.now()));

        itemService.addComment(1L, 1L, new CommentRequestDto("comment"));

        Mockito
                .verify(userRepository, Mockito.times(1))
                .findById(anyLong());
        Mockito
                .verify(itemRepository, Mockito.times(1))
                .findById(anyLong());
        Mockito
                .verify(bookingRepository, Mockito.times(1))
                .findAllByBookerAndItemAndEndBeforeOrderByStartDesc(any(User.class),
                        any(Item.class),
                        any(LocalDateTime.class));
        Mockito
                .verify(commentRepository, Mockito.times(1))
                .save(any(Comment.class));
        Mockito.verifyNoMoreInteractions(userRepository, itemRepository, commentRepository, bookingRepository, itemRequestRepository);
    }
}
