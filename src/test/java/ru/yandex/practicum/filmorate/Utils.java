package ru.yandex.practicum.filmorate;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;

public class Utils {

    public Film getFilmWithId(Long id) {
        Film film = new Film("Test Film", "Description", LocalDate.of(2022, 1, 1), 120);
        film.setId(id);
        return film;
    }

    public Film getFilmWithBlancName() {
        return new Film("", "New Film Description", LocalDate.of(2022, 1, 1), 100);
    }

    public Film getFilmWithLongDescription(int length) {
        return new Film("Test Film", generateLongDescription(length), LocalDate.of(2022, 1, 1), 120);
    }

    public Film getFilmWithSetDate(int year, int month, int day) {
        return new Film("Test Film", "New Film Description", LocalDate.of(year, month, day), 100);
    }

    public Film getFilmWithNegativeDuration() {
        return new Film("Test Film", "Description", LocalDate.of(2022, 1, 1), -1);
    }

    public Film getUpdatedFilm(Film film) {
        Film updatedFilm = new Film("Updated Film", "UpdatedDes", LocalDate.of(2022, 1, 1), 120);
        updatedFilm.setId(film.getId());
        return updatedFilm;
    }

    private String generateLongDescription(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= length; i++) {
            sb.append("a");
        }
        return sb.toString();
    }

    public User getUserWithId(Long id) {
        User user = new User("go.goro@yandex.ru", "Ryan_Gosling", "Ryan", LocalDate.of(1995, 1, 16));
        user.setId(id);
        return user;
    }

    public User getUserWithNullEmail() {
        return new User(null, "Ryan_Gosling", "Ryan", LocalDate.of(1995, 1, 16));


    }

    public User getUserWithInvalidEmail() {
        return new User("go.goroyandex.ru", "Ryan_Gosling", "Ryan", LocalDate.of(1995, 1, 16));

    }

    public User getUserWithBlankLogin() {
        return new User("go.goro@yandex.ru", "", "Ryan", LocalDate.of(1995, 1, 16));

    }

    public User getUserWithInvalidLogin() {
        return new User("go.goro@yandex.ru", "Ryan Gosling", "Ryan", LocalDate.of(1995, 1, 16));

    }

    public User getUserWithBlankName() {
        return new User("go.goro@yandex.ru", "Ryan_Gosling", "", LocalDate.of(1995, 1, 16));

    }

    public User getUserWithBirthDateInFuture() {
        return new User("go.goro@yandex.ru", "Ryan_Gosling", "Ryan", LocalDate.now().plusDays(1));

    }

    public User getUpdatedUser(User user) {
        User updatedUser = new User("newemail@yandex.ru", "new_ryan_gosling", "Ryan", LocalDate.of(1995, 1, 16));
        updatedUser.setId(user.getId());
        return updatedUser;
    }
}

