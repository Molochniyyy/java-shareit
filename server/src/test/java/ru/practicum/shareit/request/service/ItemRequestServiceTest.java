package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestPostDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;

public class ItemRequestServiceTest {
    @Test
    void createRequestTest() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        ItemRequestRepository itemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
        ItemRequestService itemRequestService = new ItemRequestServiceImpl(
                userRepository,
                itemRequestRepository,
                itemRepository
        );
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(new User(1L, "test", "test@test.com")));
        Mockito
                .when(itemRequestRepository.save(any(ItemRequest.class)))
                .thenReturn(new ItemRequest(1L,
                        "описание",
                        new User(1L, "test", "test@test.com"),
                        LocalDateTime.now())
                );
        itemRequestService.createRequest(1L, new ItemRequestPostDto("описание"));

        Mockito
                .verify(userRepository, Mockito.times(1))
                .findById(anyLong());
        Mockito
                .verify(itemRequestRepository, Mockito.times(1))
                .save(any(ItemRequest.class));

        Mockito.verifyNoMoreInteractions(itemRequestRepository, itemRepository, userRepository);
    }

    @Test
    void getRequestsTest() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        ItemRequestRepository itemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
        ItemRequestService itemRequestService = new ItemRequestServiceImpl(
                userRepository,
                itemRequestRepository,
                itemRepository
        );
        User testUser = new User(1L, "test", "test@test.com");
        ItemRequest itemRequest = new ItemRequest(1L, "описание", testUser, LocalDateTime.now());
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(testUser));
        Mockito
                .when(itemRequestRepository.findByRequesterIdIsOrderByCreatedDesc(anyLong()))
                .thenReturn(List.of(itemRequest));
        Mockito
                .when(itemRepository.findAllByRequestIn(anyCollection()))
                .thenReturn(new LinkedList<>());

        itemRequestService.getRequests(testUser.getId());

        Mockito
                .verify(userRepository, Mockito.times(1))
                .findById(anyLong());
        Mockito
                .verify(itemRequestRepository, Mockito.times(1))
                .findByRequesterIdIsOrderByCreatedDesc(anyLong());
        Mockito
                .verify(itemRepository, Mockito.times(1))
                .findAllByRequestIn(anyCollection());

        Mockito.verifyNoMoreInteractions(itemRequestRepository, itemRepository, userRepository);
    }

    @Test
    void getRequestByIdTest() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        ItemRequestRepository itemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
        ItemRequestService itemRequestService = new ItemRequestServiceImpl(
                userRepository,
                itemRequestRepository,
                itemRepository
        );
        User testUser = new User(1L, "test", "test@test.com");
        ItemRequest itemRequest = new ItemRequest(1L, "описание", testUser, LocalDateTime.now());
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(testUser));
        Mockito
                .when(itemRequestRepository.findById(anyLong()))
                .thenReturn(Optional.of(itemRequest));
        Mockito
                .when(itemRepository.findAllByRequestIn(anyCollection()))
                .thenReturn(new LinkedList<>());

        itemRequestService.getRequestById(testUser.getId(), itemRequest.getId());

        Mockito
                .verify(userRepository, Mockito.times(1))
                .findById(anyLong());
        Mockito
                .verify(itemRequestRepository, Mockito.times(1))
                .findById(anyLong());
        Mockito
                .verify(itemRepository, Mockito.times(1))
                .findAllByRequestIn(anyCollection());

        Mockito.verifyNoMoreInteractions(itemRequestRepository, itemRepository, userRepository);
    }

    @Test
    void getAllRequestsTest() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        ItemRequestRepository itemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
        ItemRequestService itemRequestService = new ItemRequestServiceImpl(
                userRepository,
                itemRequestRepository,
                itemRepository
        );
        User testUser = new User(1L, "test", "test@test.com");
        ItemRequest itemRequest = new ItemRequest(1L, "описание", testUser, LocalDateTime.now());
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(testUser));
        Mockito
                .when(itemRequestRepository.findAllByRequesterIdIsNotOrderByCreatedDesc(anyLong(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(itemRequest)));
        Mockito
                .when(itemRepository.findAllByRequestIn(anyCollection()))
                .thenReturn(new LinkedList<>());

        itemRequestService.getAllRequests(testUser.getId(), 0, 20);

        Mockito
                .verify(userRepository, Mockito.times(1))
                .findById(anyLong());
        Mockito
                .verify(itemRequestRepository, Mockito.times(1))
                .findAllByRequesterIdIsNotOrderByCreatedDesc(anyLong(), any(Pageable.class));
        Mockito
                .verify(itemRepository, Mockito.times(1))
                .findAllByRequestIn(anyCollection());

        Mockito.verifyNoMoreInteractions(itemRequestRepository, itemRepository, userRepository);
    }
}
