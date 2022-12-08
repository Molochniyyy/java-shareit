package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService service;

    @PostMapping
    public Item add(@RequestHeader("X-Sharer-User-Id") Integer userId,
                    @RequestBody ItemDto itemDto){
        return service.add(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public Item update(@RequestHeader("X-Sharer-User-Id") Integer userId,
                       @RequestBody ItemDto itemDto,
                       @PathVariable Integer itemId){
        return service.update(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public Item getById(@RequestHeader("X-Sharer-User-Id") Integer userId,
                        @PathVariable Integer itemId){
        return service.getById(userId, itemId);
    }

    @GetMapping
    public List<Item> getOfUser(@RequestHeader("X-Sharer-User-Id") Integer userId){
        return service.checkItems(userId);
    }

    @GetMapping("/search")
    public List<Item> search(@RequestHeader("X-Sharer-User-Id") Integer userId,
                             @RequestParam("text") String text){
        return service.searchItems(userId, text);
    }
}
