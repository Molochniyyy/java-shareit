package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.UserAccessException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {

    private final UserRepository userRepository;
    private Integer nextId = 1;

    private final Map<Integer, Item> items = new HashMap<>();

    @Override
    public Item add(Integer userId, Item item) {
        checkUser(userId);
        item.setId(nextId++);
        User owner = userRepository.getById(userId);
        item.setOwner(owner);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Integer userId, Integer itemId, Item item) {
        if (!Objects.equals(items.get(itemId).getOwner().getId(), userId)) {
            throw new UserAccessException("Только владелец может обновить вещь");
        }
        checkItem(itemId);
        item.setId(itemId);
        User owner = userRepository.getById(userId);
        item.setOwner(owner);
        items.put(itemId, getItem(item, itemId));
        return getItem(item, itemId);
    }

    @Override
    public Item getById(Integer userId, Integer itemId) {
        checkItem(itemId);
        return items.get(itemId);
    }

    @Override
    public List<Item> checkItemsOfUser(Integer userId) {
        List<Item> itemList = new ArrayList<>();
        for (Item item : items.values()) {
            if (Objects.equals(item.getOwner().getId(), userId)) {
                itemList.add(item);
            }
        }
        return itemList;
    }

    @Override
    public List<Item> searchItems(Integer userId, String text) {
        List<Item> itemList = new ArrayList<>();
        if (text.isEmpty() || text.isBlank()) {
            return itemList;
        }
        for (Item item : items.values()) {
            if (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                    item.getDescription().toLowerCase().contains(text.toLowerCase())) {
                if (item.getAvailable()) {
                    itemList.add(item);
                }
            }
        }
        return itemList;
    }

    private void checkUser(Integer userId) {
        if (userRepository.getById(userId) == null) {
            throw new UserNotFoundException("Пользователя с таким id не существует");
        }
    }

    private void checkItem(Integer itemId) {
        if (!items.containsKey(itemId)) {
            throw new ItemNotFoundException("Такого предмета не существует");
        }
    }

    private Item getItem(Item item, Integer itemId) {
        if (item.getName() == null) {
            item.setName(items.get(itemId).getName());
        }
        if (item.getDescription() == null) {
            item.setDescription(items.get(itemId).getDescription());
        }
        if (item.getAvailable() == null) {
            item.setAvailable(items.get(itemId).getAvailable());
        }
        if (item.getRequest() == null) {
            item.setRequest(items.get(itemId).getRequest());
        }
        if (item.getOwner() == null) {
            item.setOwner(items.get(itemId).getOwner());
        }
        return item;
    }
}
