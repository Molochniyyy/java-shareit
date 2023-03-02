package ru.practicum.shareit.request.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemWrapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestPostDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestWrapper;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    UserRepository userRepository;
    ItemRequestRepository repository;
    ItemRepository itemRepository;

    public ItemRequestServiceImpl(@Lazy UserRepository userRepository, @Lazy ItemRequestRepository repository,
                                  @Lazy ItemRepository itemRepository) {
        this.userRepository = userRepository;
        this.repository = repository;
        this.itemRepository = itemRepository;
    }

    @Override
    @Transactional
    public ItemRequestDto createRequest(Long userId, ItemRequestPostDto dto) {
        User requester = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException("Пользователь не найден")
        );
        ItemRequest itemRequest = ItemRequestWrapper.toItemRequest(dto, requester);
        return ItemRequestWrapper.toItemRequestDto(repository.save(itemRequest));
    }

    @Override
    public Collection<ItemRequestDto> getRequests(Long userId) {
        userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException("Пользователь не найден")
        );
        Collection<ItemRequest> itemRequestList = repository.findByRequesterIdIsOrderByCreatedDesc(userId);
        Collection<Item> items = itemRepository.findAllByRequestIn(itemRequestList);
        return toItemRequestDtos(itemRequestList, items);
    }

    @Override
    public ItemRequestDto getRequestById(Long userId, Long requestId) {
        userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException("Пользователь не найден")
        );
        ItemRequest itemRequest = repository.findById(requestId).orElseThrow(
                () -> new ObjectNotFoundException("Не найден запрос с id " + requestId)
        );
        Collection<Item> items = itemRepository.findAllByRequestIn(List.of(itemRequest));
        return ItemRequestWrapper.toItemRequestDto(itemRequest,
                items.stream()
                        .map(ItemWrapper::toItemDto)
                        .collect(Collectors.toList()));
    }

    @Override
    public Collection<ItemRequestDto> getAllRequests(Long userId, Integer from, Integer size) {
        userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException("Пользователь не найден")
        );
        Pageable pageable = PageRequest.of(from / size, size);
        Page<ItemRequest> itemRequests = repository.findAllByRequesterIdIsNotOrderByCreatedDesc(userId, pageable);
        Collection<Item> items = itemRepository.findAllByRequestIn(itemRequests.toList());
        return toItemRequestDtos(itemRequests.toList(), items);
    }

    private Collection<ItemRequestDto> toItemRequestDtos(Collection<ItemRequest> itemRequests,
                                                         Collection<Item> items) {
        Collection<ItemRequestDto> dtos = new LinkedList<>();
        for (ItemRequest itemRequest : itemRequests) {
            Collection<Item> itemsOfRequest = getItemsOfRequest(itemRequest.getId(), items);
            dtos.add(ItemRequestWrapper.toItemRequestDto(itemRequest, itemsOfRequest
                    .stream()
                    .map(ItemWrapper::toItemDto)
                    .collect(Collectors.toList())
            ));
        }
        return dtos;
    }

    private Collection<Item> getItemsOfRequest(Long requestId, Collection<Item> items) {
        return items.stream()
                .filter((Item item) -> item.getRequest().getId().equals(requestId))
                .collect(Collectors.toList());
    }
}
