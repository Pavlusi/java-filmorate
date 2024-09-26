package ru.yandex.practicum.filmorate.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.Utils;
import ru.yandex.practicum.filmorate.model.Film;


import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FilmController.class)
@AutoConfigureMockMvc
public class FilmControllerTest {

    private FilmController filmController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Map<Long, Film> films;

    private Utils utils;

    @BeforeEach
    public void setUp() throws Exception {
        films = new HashMap<>();
        utils = new Utils();
        reset();
    }

    private void reset() throws Exception {
        mockMvc.perform(delete("/films"));
    }

    @Test
    public void getFilms() throws Exception {
        Film film = utils.getFilmWithId(1L);
        films.put(film.getId(), film);

        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(film)));


        mockMvc.perform(get("/films")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(films.values())));

    }

    @Test
    public void createFilm() throws Exception {
        shouldSaveNewFilm();
    }

    private void shouldSaveNewFilm() throws Exception {
        Film film = utils.getFilmWithId(1L);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(film)));
    }

    @Test
    public void validateFilmModelTests() throws Exception {
        shouldReturnErrorMessageWhenNameIsBlank();
        shouldReturnErrorMessageWhenDescriptionLengthOver200();
        shouldReturnErrorMessageWhenReleaseDateBefore28_12_1895();
        shouldReturnErrorMessageWhenDurationIsNegative();
    }

    private void shouldReturnErrorMessageWhenNameIsBlank() throws Exception {
        Film film = utils.getFilmWithBlancName();

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Название не может быть пустым"));
    }

    private void shouldReturnErrorMessageWhenDescriptionLengthOver200() throws Exception {
        Film film = utils.getFilmWithLongDescription(201);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Длина описания не может превышать 200 символов"));
    }

    private void shouldReturnErrorMessageWhenReleaseDateBefore28_12_1895() throws Exception {
        Film film = utils.getFilmWithSetDate(1895, 12, 27);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Дата релиза не может быть раньше 28 декабря 1895 года"));
    }

    private void shouldReturnErrorMessageWhenDurationIsNegative() throws Exception {
        Film film = utils.getFilmWithNegativeDuration();

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Длительность фильма не может быть отрицательным числом"));
    }

    @Test
    public void updateFilm() throws Exception {
        shouldUpdateFilm();
        shouldReturnErrorMessageWhenIdIsNull();
        shouldReturnErrorMessageWhenIdDoesNotExist();

    }

    private void shouldUpdateFilm() throws Exception {
        Film film = utils.getFilmWithId(1L);
        Film updatedFilm = utils.getUpdatedFilm(film);

        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(film)));

        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedFilm)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedFilm)));
    }

    private void shouldReturnErrorMessageWhenIdIsNull() throws Exception {
        Film updatedFilm = utils.getFilmWithId(null);

        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedFilm)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("id не  может быть пустым"));
    }

    private void shouldReturnErrorMessageWhenIdDoesNotExist() throws Exception {
        Film updatedFilm = utils.getFilmWithId(2L);

        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedFilm)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Фильма с id: " + updatedFilm.getId() + " не существует"));
    }
}
