package ru.practicum.shareit.item.model;

import ru.practicum.shareit.item.dto.ItemDto;
import java.util.ArrayList;
import java.util.List;

public class ItemWrapper {
    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .available(item.getAvailable())
                .description(item.getDescription())
                .requestId(item.getRequestId())
                .build();
    }

    public static Item toItem(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .available(itemDto.getAvailable())
                .description(itemDto.getDescription())
                .requestId(itemDto.getRequestId())
                .build();
    }

    public static List<ItemDto> toListOfDto(List<Item> items) {
        List<ItemDto> itemDto = new ArrayList<>();
        for (Item item : items) {
            itemDto.add(toItemDto(item));
        }
        return itemDto;
    }
}
