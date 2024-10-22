package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor()
public class UserService {

    private final UserStorage userStorage;

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(Long id) throws NotFoundException {
        return userStorage.getUserById(id);
    }

    public User saveUser(User user) throws ValidationException {
        validateUser(user);
        return userStorage.saveUser(user);
    }

    public User updateUser(User user) throws ValidationException {
        if (user.getId() == null) {
            log.error("User id is null");
            throw new ValidationException("id не  может быть пустым");
        }
        validateUser(user);
        return userStorage.updateUser(user);
    }

    public void addingFriend(Long userId, Long friendId) throws NotFoundException {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        if (friend.getFriends() == null) {
            friend.setFriends(new HashSet<>());
        }
        user.getFriends().add(friend);
        friend.getFriends().add(user);
    }

    public void unfriending(Long userId, Long friendId) throws NotFoundException {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        if (friend.getFriends() == null) {
            friend.setFriends(new HashSet<>());
        }
        user.getFriends().remove(friend);
        friend.getFriends().remove(user);
    }

    public Collection<User> getFriends(Long userId) throws NotFoundException {
        return userStorage.getUserById(userId).getFriends();
    }

    public Collection<User> getSharedFriends(Long userId, Long otherId) throws NotFoundException {
        User user = userStorage.getUserById(userId);
        User other = userStorage.getUserById(otherId);
        Set<User> friends = new HashSet<>(user.getFriends());
        friends.retainAll(other.getFriends());
        return friends;
    }

    public void deleteAllUsers() {
        userStorage.deleteAll();
    }

    private void validateUser(User user) throws ValidationException {
        if (user.getLogin().contains(" ")) {
            log.error("Login contains space : {}", user.getLogin());
            throw new ValidationException("Логин не может содержать пробелы");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }

}
