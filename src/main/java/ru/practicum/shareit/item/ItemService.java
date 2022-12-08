package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    Item add(Integer userId, ItemDto item);

    Item update(Integer userId, Integer itemId, ItemDto itemDto);

    Item getById(Integer userId, Integer itemId);

    List<Item> checkItems(Integer userId);

    List<Item> searchItems(Integer userId, String text);
}
