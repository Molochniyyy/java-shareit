package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Service
public interface ItemService {
    ItemDto add(Long userId, Item item);

    ItemDto update(Long userId, Long itemId, Item item);

    ItemDto getById(Long userId, Long itemId);

    List<ItemDto> checkItems(Long userId);

    List<ItemDto> searchItems(String text);

    CommentDto addComment(Long userId, Long itemId, Comment comment);
}
