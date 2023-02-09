package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {
    Item add(Integer userId, Item item);

    Item update(Integer userId, Integer itemId, Item item);

    Item getById(Integer userId, Integer itemId);

    List<Item> checkItems(Integer userId);

    List<Item> searchItems(Integer userId, String text);
}
