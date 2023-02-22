package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemWrapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {


    private final ItemRepository repository;

    private final ItemRepositoryImpl itemRepository;
    private final UserRepository userRepository;


    @Override
    public ItemDto add(Long userId, Item item) {
        UserValidator.isValidIdUsers(userId);
        if (userRepository.findById(userId).isPresent()) {
            item.setOwnerId(userId);
            ItemValidator.isValidCreateItem(item);
            return ItemWrapper.toItemDto(repository.save(item));
        } else {
            throw new UserNotFoundException("Пользователя с таким id не существует");
        }

    }

    @Override
    public ItemDto update(Long userId, Long itemId, Item item) {
        UserValidator.isValidIdUsers(userId);
        return itemRepository.update(userId, itemId, item);
    }

    @Override
    public ItemDto getById(Long userId, Long itemId) {
        Item item = repository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Такой вещи не существует"));
        return ItemWrapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> checkItems(Long userId) {
        return null;
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        return null;
    }

    @Override
    public CommentDto addComment(Long userId, Long itemId, Comment comment) {
        return null;
    }
}
