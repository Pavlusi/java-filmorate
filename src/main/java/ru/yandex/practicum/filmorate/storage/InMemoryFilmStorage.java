package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Collection<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(Long id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException("Фильм с id: " + id + " не найден");
        }
        return films.get(id);
    }

    @Override
    public Film save(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film updatedFilm) throws NotFoundException {
        if (!films.containsKey(updatedFilm.getId())) {
            log.error("Film id not found : {}", updatedFilm.getId());
            throw new NotFoundException("Фильма с id: " + updatedFilm.getId() + " не существует");
        }
        updateFilmDate(updatedFilm);
        return films.get(updatedFilm.getId());
    }

    @Override
    public void deleteAll() {
        films.clear();
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
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

}
