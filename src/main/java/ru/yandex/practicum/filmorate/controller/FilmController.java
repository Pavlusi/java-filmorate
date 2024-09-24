package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;


import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();
    private final LocalDate minReleaseDate = LocalDate.of(1895, Month.DECEMBER, 28);


    @GetMapping
    public Collection<Film> getFilms() {
        log.info("Get all films");
        return films.values().stream().toList();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        validateFilm(film);
        film.setId(getNextId());
        log.info("New film created: {}", film);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (film.getId() == null) {
            log.error("Film id is null");
            throw new ValidationException("id не  может быть пустым");

        } else if (!films.containsKey(film.getId())) {
            log.error("Film id not found : {}", film.getId());
            throw new ValidationException("Фильма с id: " + film.getId() + " не существует");
        }
        validateFilm(film);
        log.info("Update film: {}", film);
        return updateFilmDate(film);
    }

    @DeleteMapping
    public void deleteAll() {
        films.clear();
    }

    private void validateFilm(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(minReleaseDate)) {
            log.error("Film release date is before minimum date: {}", film.getReleaseDate());
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
    }

    private Film updateFilmDate(Film updatedFilm) {
        Film oldFilm = films.get(updatedFilm.getId());
        if (updatedFilm.getName() != null) {
            oldFilm.setName(updatedFilm.getName());
        }
        if (updatedFilm.getDescription() != null) {
            oldFilm.setDescription(updatedFilm.getDescription());
        }
        if (updatedFilm.getReleaseDate() != null) {
            oldFilm.setReleaseDate(updatedFilm.getReleaseDate());
        }
        if (updatedFilm.getDuration() != null) {
            oldFilm.setDuration(updatedFilm.getDuration());
        }
        return oldFilm;
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}
