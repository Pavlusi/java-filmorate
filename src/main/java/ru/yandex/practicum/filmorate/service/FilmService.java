package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAll();
    }

    public Film getFilmById(Long id) {
        return filmStorage.getFilmById(id);
    }

    public Film saveFilm(Film film) {
        return filmStorage.save(film);
    }

    public Film updateFilm(Film film) {
       return filmStorage.update(film);
    }

    public Collection<Film> getPopularFilms(Integer count) {
        if (count == null) {
            count = 10;
        }
        return filmStorage.getAll()
               .stream()
               .sorted((film1, film2) -> Integer.compare(film1.getUsersWhoLiked().size(), film2.getUsersWhoLiked().size()))
               .limit(count)
                .toList();
    }

    public void likeFilm(Long filmId, Long userId) {
        Film film = getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        if(film.getUsersWhoLiked() == null){
            film.setUsersWhoLiked(new HashSet<>());
        }
        if(user.getLikedFilms() == null){
            user.setLikedFilms(new HashSet<>());
        }
        film.getUsersWhoLiked().add(user);
        user.getLikedFilms().add(film);
    }

    public void unlikeFilm(Long filmId, Long userId) {
        Film film = getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        film.getUsersWhoLiked().remove(user);
        user.getLikedFilms().remove(film);
    }

    public void deleteAllFilms() {
        filmStorage.deleteAll();
    }

}
