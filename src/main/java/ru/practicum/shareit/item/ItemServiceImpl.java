package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingWrapper;
import ru.practicum.shareit.exceptions.AddCommentWithoutBookingException;
import ru.practicum.shareit.exceptions.EmptyCommentException;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.CommentWrapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemWrapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserValidator;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {


    private final ItemRepository repository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRepositoryImpl itemRepository;
    private final UserRepository userRepository;


    @Override
    public ItemDto add(Long userId, Item item) {
        UserValidator.isValidIdUsers(userId);
        if (userRepository.findById(userId).isPresent()) {
            item.setOwnerId(userId);
            ItemValidator.isValidCreateItem(item);
            return ItemWrapper.toItemDto(repository.save(item));
        } else {
            throw new UserNotFoundException("Пользователя с таким id не существует");
        }

    }

    @Override
    public ItemDto update(Long userId, Long itemId, Item item) {
        UserValidator.isValidIdUsers(userId);
        return itemRepository.update(userId, itemId, item);
    }

    @Override
    public ItemDto getById(Long userId, Long itemId) {
        Item item = repository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Такой вещи не существует"));
        ItemDto itemDto = ItemWrapper.toItemDto(item);
        updateComments(itemId, itemDto);

        if (userId.equals(item.getOwnerId())) {
            List<Booking> bookings = bookingRepository.findBookingByItemId(itemId);
            Collections.sort(bookings);
            if (!bookings.isEmpty()) {
                itemDto.setNextBooking(BookingWrapper.toBookingDtoItem(bookings.get(0)));
                itemDto.setLastBooking(BookingWrapper.toBookingDtoItem(bookings.get(bookings.size() - 1)));
            }
            return itemDto;
        } else {
            List<Booking> bookings = bookingRepository.findBookingByItemId(itemId);
            Collections.sort(bookings);
            return itemDto;
        }
    }

    @Override
    public List<ItemDto> checkItems(Long userId) {
        List<Item> itemsByOwnerId = repository.findItemsByOwnerId(userId);
        List<ItemDto> result = new ArrayList<>();
        if (!itemsByOwnerId.isEmpty()) {
            for (Item item : itemsByOwnerId) {
                List<Booking> bookings = bookingRepository.findBookingByItemId(item.getId());
                ItemDto itemDto = ItemWrapper.toItemDto(item);
                Collections.sort(bookings);
                if (!bookings.isEmpty()) {
                    itemDto.setNextBooking(BookingWrapper.toBookingDtoItem(bookings.get(0)));
                    itemDto.setLastBooking(BookingWrapper.toBookingDtoItem(bookings.get(bookings.size() - 1)));
                }
                result.add(itemDto);
                Collections.sort(result);
            }
        }
        return result;
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        List<Item> items = new ArrayList<>();
        if (!text.isEmpty()) {
            String lowerCase = text.toLowerCase();
            List<Item> itemsOfSearch = repository.searchItems(lowerCase);
            for (Item item : itemsOfSearch) {
                if (item.getAvailable()) {
                    items.add(item);
                }
            }
        }
        return ItemWrapper.toListOfDto(items);
    }

    @Override
    public CommentDto addComment(Long userId, Long itemId, Comment comment) {
        boolean isBookerId = false;
        ItemDto itemDto = getById(userId, itemId);
        List<Booking> bookingByItemId = bookingRepository.findBookingByItemId(itemId);
        List<CommentDto> comments = new ArrayList<>();
        if (comment == null) {
            itemDto.setComments(comments);
        } else if (comment.getText().isEmpty()) {
            throw new EmptyCommentException("Пустой комментарий");
        } else {
            for (Booking booking : bookingByItemId) {
                if (userId.equals(booking.getBookerId())
                        && booking.getStatus().equals(BookingStatus.APPROVED)
                        && !booking.getStart().isAfter(LocalDateTime.now())) {
                    isBookerId = true;
                    break;
                }
            }
            if (isBookerId) {
                comment.setItemId(itemId);
                comment.setCreated(LocalDateTime.now());
                comment.setAuthorId(userId);
                commentRepository.save(comment);
                comments.add(CommentWrapper.toCommentDto(comment));
                itemDto.setComments(comments);
            } else {
                throw new AddCommentWithoutBookingException("Booking not found");
            }
        }
        CommentDto commentDto = CommentWrapper.toCommentDto(comment);
        commentDto.setAuthorName(
                userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"))
                        .getName());
        return commentDto;
    }

    private void updateComments(Long itemId, ItemDto itemDto) {
        List<Comment> comments = commentRepository.findCommentsByItemId(itemId);
        List<CommentDto> commentDtos = new ArrayList<>();
        for (Comment comment : comments) {
            Long userId = comment.getAuthorId();
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
            CommentDto commentDto = CommentWrapper.toCommentDto(comment);
            commentDto.setAuthorName(user.getName());
            commentDtos.add(commentDto);
        }
        itemDto.setComments(commentDtos);
    }
}
