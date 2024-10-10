package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor()
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LocalDate minReleaseDate = LocalDate.of(1895, Month.DECEMBER, 28);

    public Collection<Film> getAllFilms() {
        return filmStorage.getAll();
    }

    public Film getFilmById(Long id) {
        return filmStorage.getFilmById(id);
    }

    public Film saveFilm(Film film) throws ValidationException {
        validateFilmReleaseDate(film);
        return filmStorage.save(film);
    }

    public Film updateFilm(Film film) throws ValidationException {
        if (film.getId() == null) {
            log.error("Film id is null");
            throw new ValidationException("id не  может быть пустым");
        }
        validateFilmReleaseDate(film);
        return filmStorage.update(film);
    }

    public Collection<Film> getPopularFilms(Integer count) {
        return filmStorage.getAll()
                .stream()
                .sorted((film1, film2) -> Integer.compare(film2.getUsersWhoLiked().size(), film1.getUsersWhoLiked().size()))
                .limit(count)
                .toList();
    }

    public void likeFilm(Long filmId, Long userId) {
        Film film = getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        if (film.getUsersWhoLiked() == null) {
            film.setUsersWhoLiked(new HashSet<>());
        }
        if (user.getLikedFilms() == null) {
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

    private void validateFilmReleaseDate(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(minReleaseDate)) {
            log.error("Film release date is before minimum date: {}", film.getReleaseDate());
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
    }

}
