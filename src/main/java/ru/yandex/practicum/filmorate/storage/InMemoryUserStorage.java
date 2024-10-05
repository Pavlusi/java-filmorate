package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Long id) throws NotFoundException {
        if (!users.containsKey(id)){
            throw new NotFoundException("Пользователь с id: " + id + " не найден");
        }
        return users.get(id);
    }

    @Override
    public User saveUser(User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) throws NotFoundException {
        if (!users.containsKey(user.getId())) {
            log.error("User id not exist : {}", user.getId());
            throw new NotFoundException("Пользователя  с id: " + user.getId() + " не существует");
        }
        updateUserDate(user);
        return users.get(user.getId());
    }

    @Override
    public void deleteAll() {
      users.clear();
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private User updateUserDate(User updatedUser) {
        User oldUser = users.get(updatedUser.getId());
        if (updatedUser.getEmail() != null) {
            oldUser.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getName() != null) {
            oldUser.setName(updatedUser.getName());
        }
        if (updatedUser.getLogin() != null) {
            oldUser.setLogin(updatedUser.getLogin());
        }
        if (updatedUser.getBirthday() != null) {
            oldUser.setBirthday(updatedUser.getBirthday());
        }
        return oldUser;
    }
}
