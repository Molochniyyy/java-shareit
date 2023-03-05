package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

@Service
public interface ItemService {
    ItemDto add(Long userId, ItemDto itemDto);

    ItemDto update(Long userId, Long itemId, ItemDto itemDto);

    ItemDto getById(Long userId, Long itemId);

    Collection<ItemDto> getItems(Long userId, Integer from, Integer size);

    Collection<ItemDto> searchItems(String text, Integer from, Integer size);

    CommentDto addComment(Long userId, Long itemId, CommentRequestDto commentRequestDto);
}
