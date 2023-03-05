package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.BookingWrapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.UnauthorizedAccessException;
import ru.practicum.shareit.exceptions.UnsupportedOperationException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.CommentWrapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemWrapper;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.validator.ItemValidator;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.validator.UserValidator;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {


    private final ItemRepository repository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;


    @Override
    @Transactional
    public ItemDto add(Long userId, ItemDto itemDto) {
        ItemValidator.isValidCreateItem(itemDto);
        UserValidator.isValidIdUsers(userId);
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException("Пользователь не найден.")
        );
        ItemRequest itemRequest = null;
        if (itemDto.getRequestId() != null) {
            itemRequest = itemRequestRepository.findById(itemDto.getRequestId()).orElseThrow(
                    () -> new ObjectNotFoundException("Запрос не найден")
            );
        }
        return ItemWrapper.toItemDto(repository.save(ItemWrapper.toItem(itemDto, user, itemRequest)));

    }

    @Override
    @Transactional
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        UserValidator.isValidIdUsers(userId);
        Item item = repository.findById(itemId).orElseThrow(
                () -> new ObjectNotFoundException("Вещь не найдена")
        );
        if (!item.getOwner().getId().equals(userId)) {
            throw new UnauthorizedAccessException("Id пользователя(" + userId + ") и владельца не совпадают");
        }
        if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        return ItemWrapper.toItemDto(repository.save(item), commentRepository.findByItemIsOrderByCreatedDesc(item)
                .stream()
                .map(CommentWrapper::toCommentDto)
                .collect(toList()));
    }

    @Override
    public ItemDto getById(Long userId, Long itemId) {
        Item item = repository.findById(itemId)
                .orElseThrow(() -> new ObjectNotFoundException("Такой вещи не существует"));
        if (userId.equals(item.getOwner().getId())) {
            return ItemWrapper.toItemDto(item,
                    BookingWrapper.toBookingDtoItem(bookingRepository.findFirstByItemIsAndEndBeforeOrderByEndDesc(item,
                            LocalDateTime.now()).orElse(null)),
                    BookingWrapper.toBookingDtoItem(bookingRepository.findFirstByItemIsAndStartAfterOrderByStartAsc(item,
                            LocalDateTime.now()).orElse(null)),
                    commentRepository.findByItemIsOrderByCreatedDesc(item)
                            .stream()
                            .map(CommentWrapper::toCommentDto)
                            .collect(toList()));
        }
        return ItemWrapper.toItemDto(item, commentRepository.findByItemIsOrderByCreatedDesc(item)
                .stream()
                .map(CommentWrapper::toCommentDto)
                .collect(toList()));
    }

    @Override
    public Collection<ItemDto> getItems(Long userId, Integer from, Integer size) {
        User owner = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException("Не найден пользователь с id " + userId)
        );
        Pageable page = PageRequest.of(from / size, size);
        Page<Item> items = repository.findAllByOwnerIsOrderByIdAsc(owner, page);
        Map<Item, List<Comment>> comments = commentRepository.findAllByItemInOrderByItem(items.getContent())
                .stream().collect(groupingBy(Comment::getItem));
        Map<Item, List<Booking>> bookings = bookingRepository.findAllByStatusOrderByStartDesc(BookingStatus.APPROVED)
                .stream().collect(groupingBy(Booking::getItem));
        Collection<ItemDto> itemDtos = new ArrayList<>();
        for (Item item : items) {
            itemDtos.add(ItemWrapper.toItemDto(item,
                    bookings.getOrDefault(item, Collections.emptyList()),
                    comments.getOrDefault(item, Collections.emptyList())
                            .stream().map(CommentWrapper::toCommentDto).collect(toList())));
        }
        return itemDtos;
    }

    @Override
    public Collection<ItemDto> searchItems(String text, Integer from, Integer size) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        Pageable page = PageRequest.of(from / size, size);
        return repository.searchItems(text, page).stream()
                .map(ItemWrapper::toItemDto)
                .collect(toList());
    }

    @Override
    @Transactional
    public CommentDto addComment(Long userId, Long itemId, CommentRequestDto commentDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException("Не найден пользователь с id " + userId)
        );
        Item item = repository.findById(itemId).orElseThrow(
                () -> new ObjectNotFoundException("Не найден предмет с id " + itemId)
        );
        if (bookingRepository.findAllByBookerAndItemAndEndBeforeOrderByStartDesc(user,
                item,
                LocalDateTime.now()).size() == 0) {
            throw new UnsupportedOperationException("Нельзя оставить комментарий," +
                    " так как данный предмет не был арендован");
        }
        if (commentDto.getText().isBlank()) {
            throw new ValidationException("Попытка добавить пустой комментарий");
        }
        Comment comment = CommentWrapper.toComment(commentDto);
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        return CommentWrapper.toCommentDto(commentRepository.save(comment));
    }
}
