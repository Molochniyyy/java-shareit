package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository repository;

    @Override
    public User add(User user){
        log.info("Попытка добавить пользователя");
        return repository.add(user);
    }

    @Override
    public User update(Integer id, User user){
        log.info("Попытка обносить пользователя в id = {}", id);
        return repository.update(id, user);
    }

    @Override
    public List<User> get(){
        log.info("Попытка получить всех пользователей");
        return repository.get();
    }

    @Override
    public User getById(Integer id){
        log.info("Попытка получить пользователя с id = {}", id);
        return repository.getById(id);
    }

    @Override
    public void delete(Integer id){
        repository.delete(id);
        log.info("Удален пользователь с id = {}", id);
    }
}
