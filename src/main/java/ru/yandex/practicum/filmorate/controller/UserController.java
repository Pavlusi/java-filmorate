package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();


    @GetMapping
    public Collection<User> getUsers() {
        log.info("Get all users");
        return users.values().stream().toList();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        validateUser(user);
        user.setId(getNextId());
        log.info("Created new user: {}", user);
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (user.getId() == null) {
            log.error("User id is null");
            throw new ValidationException("id не  может быть пустым");
        } else if (!users.containsKey(user.getId())) {
            log.error("User id not exist : {}", user.getId());
            throw new ValidationException("Пользователя  с id: " + user.getId() + " не существует");
        }
        validateUser(user);

        log.info("Updated user: {}", user);
        return updateUserDate(user);
    }

    private void validateUser(User user) throws ValidationException {
        if (user.getLogin().contains(" ")) {
            log.error("Login contains space : {}", user.getLogin());
            throw new ValidationException("Логин не может содержать пробелы");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
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

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
