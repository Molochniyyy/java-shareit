package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.ConflictEmailException;
import ru.practicum.shareit.exceptions.FailEmailException;
import ru.practicum.shareit.exceptions.UserNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository{

    private Integer nextId = 1;

    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User add(User user){
        emailCheck(user);
        user.setId(nextId++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(Integer id, User user){
        idCheck(id);
        if (user.getEmail() != null) {
            if (!user.getEmail().contains("@")){
                throw new FailEmailException("Email не содержит символа - @");
            }
            for (User u : users.values()) {
                if (u.getEmail().equals(user.getEmail())){
                    throw new ConflictEmailException("Email уже используется");
                }
            }
        }
        if(user.getName()==null){
            user.setName(users.get(id).getName());
        }
        if(user.getEmail()==null){
            user.setEmail(users.get(id).getEmail());
        }
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @Override
    public User getById(Integer id){
        idCheck(id);
        return users.get(id);
    }

    @Override
    public void delete(Integer id){
        idCheck(id);
        users.remove(id);
    }

    @Override
    public List<User> get(){
        return new ArrayList<>(users.values());
    }

    private void emailCheck(User user){
        if (user.getEmail() == null) {
            throw new FailEmailException("Email отсутствует");
        }
        if (!user.getEmail().contains("@")){
            throw new FailEmailException("Email не содержит символа - @");
        }
        for (User u : users.values()){
            if (u.getEmail().equals(user.getEmail())) {
                throw new ConflictEmailException("Email уже используется");
            }
        }
    }

    private void idCheck(Integer id){
        if (!users.containsKey(id)){
            throw new UserNotFoundException("Пользователя с таким id не существует");
        }
    }
}
