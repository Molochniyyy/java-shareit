package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserWrapper;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final UserRepositoryImpl userRepositoryImpl;


    @Override
    public UserDto saveUser(User user) {
        UserValidator.isValidUserToNull(user);
        UserValidator.isValidEmailUser(user);
        User newUser = userRepository.save(user);
        return UserWrapper.toUserDto(newUser);
    }

    @Override
    public UserDto updateUser(Long userId, User user) {
        return userRepositoryImpl.update(userId, user);
    }

    @Override
    public List<UserDto> getUsers() {
        return UserWrapper.toListOfDto(userRepository.findAll());
    }

    @Override
    public UserDto findUserById(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return UserWrapper.toUserDto(user);
        } else {
            throw new UserNotFoundException("Пользователя с таким id не существует");
        }
    }

    @Override
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }
}
