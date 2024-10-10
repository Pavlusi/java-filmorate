package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor()
public class UserController {

    private final UserService userService;

    @GetMapping
    public Collection<User> getUsers() {
        log.info("Get all users");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        log.info("Get user with id {}", id);
        return userService.getUserById(id);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("Created new user: {}", user);
        return userService.saveUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Updated user: {}", user);
        return userService.updateUser(user);
    }

    @DeleteMapping
    public void deleteAll() {
        userService.deleteAllUsers();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addingFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Added friend: {}", friendId);
        userService.addingFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void unfriending(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Unfrigend friend: {}", friendId);
        userService.unfriending(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable Long id) {
        log.info("Get friends of user: {}", id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getSharedFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("Get shared friends of user: {}", id);
        return userService.getSharedFriends(id, otherId);
    }

}
