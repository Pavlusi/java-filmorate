package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getAllUsers(){
        return userStorage.getAllUsers();
    }

    public User saveUser(User user){
       return userStorage.saveUser(user);
    }

    public User updateUser(User user){
        return userStorage.updateUser(user);
    }

    public void deleteAllUsers(){
        userStorage.deleteAll();
    }
}
