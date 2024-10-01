package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAll();
    }

    public Film saveFilm(Film film) {
        return filmStorage.save(film);
    }

    public Film updateFilm(Film film) {
       return filmStorage.update(film);
    }

    public void deleteAllFilms() {
        filmStorage.deleteAll();
    }

}
