package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService service;

    @PostMapping
    public ItemDto add(@RequestHeader("X-Sharer-User-Id") Integer userId,
                       @RequestBody ItemDto itemDto) {
        return ItemWrapper.toItemDto(service.add(userId, ItemWrapper.toItem(itemDto)));
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Integer userId,
                          @RequestBody ItemDto itemDto,
                          @PathVariable Integer itemId) {
        return ItemWrapper.toItemDto(service.update(userId, itemId, ItemWrapper.toItem(itemDto)));
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@RequestHeader("X-Sharer-User-Id") Integer userId,
                           @PathVariable Integer itemId) {
        return ItemWrapper.toItemDto(service.getById(userId, itemId));
    }

    @GetMapping
    public List<ItemDto> getOfUser(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return ItemWrapper.toListOfDto(service.checkItems(userId));
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                @RequestParam("text") String text) {
        return ItemWrapper.toListOfDto(service.searchItems(userId, text));
    }
}
