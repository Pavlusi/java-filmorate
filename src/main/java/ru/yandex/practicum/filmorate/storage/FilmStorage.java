package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;

public interface FilmStorage {

    public Collection<Film> getAll();

    public Film getFilmById(Long id);

    public Film save(Film film);

    public Film update(Film film);

    public void deleteAll();


}
