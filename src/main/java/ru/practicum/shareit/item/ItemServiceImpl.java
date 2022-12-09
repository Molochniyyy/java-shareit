package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.FailItemException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository repository;

    @Override
    public Item add(Integer userId, Item item) {
        checkItem(item);
        log.info("Попытка добавить вещь");
        return repository.add(userId, item);
    }

    @Override
    public Item update(Integer userId, Integer itemId, Item item) {
        log.info("Попытка обновить вещь с id = {}", itemId);
        return repository.update(userId, itemId, item);
    }

    @Override
    public Item getById(Integer userId, Integer itemId) {
        log.info("Попытка получить вещь с id = {}", itemId);
        return repository.getById(userId, itemId);
    }

    @Override
    public List<Item> checkItems(Integer userId) {
        log.info("Попытка получить список вещей пользователя с id = {}", userId);
        return repository.checkItemsOfUser(userId);
    }

    @Override
    public List<Item> searchItems(Integer userId, String text) {
        log.info("Попытка получить список вещей где сожержится строка - {}", text);
        return repository.searchItems(userId, text);
    }

    private void checkItem(Item item) {
        if (item.getAvailable() == null) {
            throw new FailItemException("Не указан параметр available");
        }
        if (item.getName() == null || item.getName().equals("")) {
            throw new FailItemException("Пустое или незаполненное имя");
        }
        if (item.getDescription() == null || item.getDescription().equals("")) {
            throw new FailItemException("Пустое или незаполненное описание");
        }
    }
}
