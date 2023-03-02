package ru.practicum.shareit.item.model;

import ru.practicum.shareit.booking.dto.BookingDtoItem;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public class ItemWrapper {
    public static ItemDto toItemDto(Item item, Collection<CommentDto> comments) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .available(item.getAvailable())
                .description(item.getDescription())
                .requestId(item.getRequest() != null ? item.getRequest().getId() : null)
                .comments(comments)
                .build();
    }

    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .available(item.getAvailable())
                .description(item.getDescription())
                .requestId(item.getRequest() != null ? item.getRequest().getId() : null)
                .build();
    }

   /* public static Item toItem(ItemDto itemDto, User user, ItemRequest itemRequest) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .available(itemDto.getAvailable())
                .description(itemDto.getDescription())
                .owner(user)
                .request(itemRequest)
                .build();
    }*/
   public static Item toItem(ItemDto itemDto, User user, ItemRequest itemRequest) {
       return new Item(itemDto.getId(),
               itemDto.getName(),
               itemDto.getDescription(),
               itemDto.getAvailable(),
               user,
               itemRequest);
   }

    public static ItemDto toItemDto(Item item,
                                    BookingDtoItem lastBooking,
                                    BookingDtoItem nextBooking,
                                    Collection<CommentDto> comments) {
        return ItemDto.builder()
                .id(item.getId())
                .description(item.getDescription())
                .available(item.getAvailable())
                .name(item.getName())
                .requestId(item.getRequest() != null ? item.getRequest().getId() : null)
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(comments)
                .build();
    }
}
