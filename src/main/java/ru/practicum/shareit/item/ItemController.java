package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemServiceImpl service;

    @PostMapping()
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody Item item) {
        return service.add(userId, item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto editItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                            @PathVariable Long itemId,
                            @RequestBody Item item) {
        return service.update(userId, itemId, item);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        return service.getById(userId, itemId);
    }

    @GetMapping()
    public List<ItemDto> getItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return service.checkItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsByRequest(@RequestParam String text) {
        return service.searchItems(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addCommentToItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @PathVariable Long itemId,
                                       @RequestBody Comment comment) {
        return service.addComment(userId, itemId, comment);
    }
}
