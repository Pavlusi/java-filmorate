package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    public Collection<User> getAllUsers();

    public User getUserById(Long id);

    public User saveUser(User user);

    public User updateUser(User user);

    public void deleteAll();
}
