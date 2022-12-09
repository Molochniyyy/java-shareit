package ru.practicum.shareit.item;

import java.util.List;

public interface ItemRepository {

    Item add(Integer userId, Item item);

    Item update(Integer userId, Integer itemId, Item item);

    Item getById(Integer userId, Integer itemId);

    List<Item> checkItemsOfUser(Integer userId);

    List<Item> searchItems(Integer userId, String text);
}
