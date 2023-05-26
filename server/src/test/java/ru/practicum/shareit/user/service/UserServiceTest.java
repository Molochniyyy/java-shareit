package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

public class UserServiceTest {
    @Test
    void createUserTest() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserServiceImpl(userRepository);
        User testUser = new User(1L, "testname", "test@mail.com");
        Mockito
                .when(userRepository.save(any(User.class)))
                .thenReturn(testUser);

        userService.saveUser(new UserDto());

        Mockito
                .verify(userRepository, Mockito.times(1))
                .save(any(User.class));
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    void getUserTest() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserServiceImpl(userRepository);
        User testUser = new User(1L, "testname", "test@mail.com");
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(testUser));

        userService.findUserById(1L);

        Mockito
                .verify(userRepository, Mockito.times(1))
                .findById(anyLong());
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    void updateUserTest() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserServiceImpl(userRepository);
        User testUser = new User(1L, "testname", "test@mail.com");
        Mockito
                .when(userRepository.findById(1L))
                .thenReturn(Optional.of(testUser));
        Mockito
                .when(userRepository.save(any(User.class)))
                .then(org.mockito.AdditionalAnswers.returnsFirstArg());

        UserDto answer = userService.updateUser(1L, new UserDto(null, "updatedName", null));

        Assertions.assertEquals(answer.getName(), "updatedName");
        Assertions.assertEquals(answer.getEmail(), testUser.getEmail());

        answer = userService.updateUser(1L, new UserDto(null, null, "updated@mail.com"));
        Assertions.assertEquals(answer.getName(), testUser.getName());
        Assertions.assertEquals(answer.getEmail(), "updated@mail.com");

        answer = userService.updateUser(1L, new UserDto(null, "updatedName", "updated@mail.com"));
        Assertions.assertEquals(answer.getName(), "updatedName");
        Assertions.assertEquals(answer.getEmail(), "updated@mail.com");

        Mockito
                .verify(userRepository, Mockito.times(3))
                .findById(anyLong());
        Mockito
                .verify(userRepository, Mockito.times(3))
                .save(any(User.class));
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    void deleteUserTest() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserServiceImpl(userRepository);

        userService.deleteUserById(1L);

        Mockito
                .verify(userRepository, Mockito.times(1))
                .deleteById(anyLong());
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    void getUsersTest() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserServiceImpl(userRepository);
        User testUser = new User(1L, "testname", "test@mail.com");
        Mockito
                .when(userRepository.findAll())
                .thenReturn(List.of(testUser));

        userService.getUsers();

        Mockito
                .verify(userRepository, Mockito.times(1))
                .findAll();
        Mockito.verifyNoMoreInteractions(userRepository);
    }
}
