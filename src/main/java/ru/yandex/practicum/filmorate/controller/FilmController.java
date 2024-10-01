package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;


import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;
    private final LocalDate minReleaseDate = LocalDate.of(1895, Month.DECEMBER, 28);

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> getFilms() {
        log.info("Get all films");
        return filmService.getAllFilms();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        validateFilmReleaseDate(film);
        log.info("New film created: {}", film);
        return filmService.saveFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (film.getId() == null) {
            log.error("Film id is null");
            throw new ValidationException("id не  может быть пустым");
        }
        validateFilmReleaseDate(film);
        log.info("Update film: {}", film);
        return filmService.updateFilm(film);
    }

    @DeleteMapping
    public void deleteAll() {
        filmService.deleteAllFilms();
    }

    private void validateFilmReleaseDate(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(minReleaseDate)) {
            log.error("Film release date is before minimum date: {}", film.getReleaseDate());
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
    }


}
