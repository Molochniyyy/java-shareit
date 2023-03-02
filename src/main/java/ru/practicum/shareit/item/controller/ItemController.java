package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.exceptions.UnsupportedOperationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService service;

    @PostMapping()
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @Validated({Create.class}) @RequestBody ItemDto item) {
        return service.add(userId, item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto editItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                            @PathVariable Long itemId,
                            @Validated({Update.class}) @RequestBody ItemDto item) {
        return service.update(userId, itemId, item);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        return service.getById(userId, itemId);
    }

    @GetMapping()
    public Collection<ItemDto> getItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @RequestParam(name = "from", defaultValue = "0") Integer from,
                                        @RequestParam(name = "size", defaultValue = "20") Integer size) {
        if (from < 0 || size < 1) {
            throw new UnsupportedOperationException("Неверные параметры запроса");
        }
        return service.getItems(userId, from, size);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItems(@RequestParam String text,
                                           @RequestParam(name = "from", defaultValue = "0") Integer from,
                                           @RequestParam(name = "size", defaultValue = "20") Integer size) {
        if (from < 0 || size < 1) {
            throw new UnsupportedOperationException("Неверные параметры запроса");
        }
        return service.searchItems(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addCommentToItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @PathVariable Long itemId,
                                       @Validated({Create.class}) @RequestBody CommentRequestDto comment) {
        return service.addComment(userId, itemId, comment);
    }
}
