package ru.practicum.shareit.item;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.OwnerNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemWrapper;

import java.util.*;


@Component
public class ItemRepositoryImpl {


    private final ItemRepository repository;

    public ItemRepositoryImpl(@Lazy ItemRepository repository) {
        this.repository = repository;
    }

    public ItemDto update(Long userId, Long itemId, Item item) {
        Item newItem = repository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Вещь не найдена"));
        if (item.getName() != null) {
            newItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            newItem.setDescription(item.getDescription());
        }
        if (item.getRequestId() != null) {
            newItem.setRequestId(item.getRequestId());
        }
        if (item.getAvailable() != null) {
            newItem.setAvailable(item.getAvailable());
        }

        if (Objects.equals(newItem.getOwnerId(), userId)) {
            newItem.setOwnerId(userId);
            repository.save(newItem);
            return ItemWrapper.toItemDto(newItem);
        } else {
            throw new OwnerNotFoundException("Пользователь не является владельцем вещи");
        }

    }

}
