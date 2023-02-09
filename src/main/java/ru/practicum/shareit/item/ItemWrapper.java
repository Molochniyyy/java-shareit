package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.ArrayList;
import java.util.List;

public class ItemWrapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                item.getRequest()
        );
    }

    public static Item toItem(ItemDto itemDto) {
        return new Item(itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                itemDto.getOwner(),
                itemDto.getRequest());
    }

    public static List<ItemDto> toListOfDto(List<Item> items) {
        List<ItemDto> itemDto = new ArrayList<>();
        for (Item item : items) {
            itemDto.add(toItemDto(item));
        }
        return itemDto;
    }
}
