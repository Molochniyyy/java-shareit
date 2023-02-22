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

        newItem.setName(item.getName());
        newItem.setDescription(item.getDescription());
        newItem.setRequestId(item.getRequestId());
        newItem.setAvailable(item.getAvailable());

        if (Objects.equals(item.getOwnerId(), userId)) {
            newItem.setOwnerId(userId);
            return ItemWrapper.toItemDto(newItem);
        } else {
            throw new OwnerNotFoundException("Пользователь не является владельцем вещи");
        }

    }

}
