package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.FailItemException;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService{

    private final ItemRepository repository;

    @Override
    public Item add(Integer userId, ItemDto itemDto){
        checkItem(itemDto);
        log.info("Попытка добавить вещь");
        return repository.add(userId, ItemWrapper.toItem(itemDto));
    }

    @Override
    public Item update(Integer userId, Integer itemId, ItemDto itemDto){
        log.info("Попытка обновить вещь с id = {}", itemId);
        return repository.update(userId, itemId, ItemWrapper.toItem(itemDto));
    }

    @Override
    public Item getById(Integer userId, Integer itemId){
        log.info("Попытка получить вещь с id = {}", itemId);
        return repository.getById(userId, itemId);
    }

    @Override
    public List<Item> checkItems(Integer userId){
        log.info("Попытка получить список вещей пользователя с id = {}", userId);
        return repository.checkItemsOfUser(userId);
    }

    @Override
    public List<Item> searchItems(Integer userId, String text){
        log.info("Попытка получить список вещей где сожержится строка - {}", text);
        return repository.searchItems(userId, text);
    }

    private void checkItem(ItemDto itemDto){
        if(itemDto.getAvailable()==null){
            throw new FailItemException("Не указан параметр available");
        }
        if(itemDto.getName()==null || itemDto.getName().equals("")){
            throw new FailItemException("Пустое или незаполненное имя");
        }
        if(itemDto.getDescription()==null || itemDto.getDescription().equals("")){
            throw new FailItemException("Пустое или незаполненное описание");
        }
    }
}
