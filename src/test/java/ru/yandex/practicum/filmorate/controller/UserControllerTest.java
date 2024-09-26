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
import ru.yandex.practicum.filmorate.model.User;


import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    private UserController userController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Map<Long, User> users;

    private Utils utils;

    @BeforeEach
    public void setUp() throws Exception {
        users = new HashMap<>();
        utils = new Utils();
        reset();
    }

    private void reset() throws Exception {
        mockMvc.perform(delete("/users"));
    }

    @Test
    public void getUsers() throws Exception {
        User user = utils.getUserWithId(1L);
        users.put(user.getId(), user);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));


        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(users.values())));
    }

    @Test
    public void createUser() throws Exception {
        shouldSaveNewUser();
        shouldReplaceNameIfItIsBlank();
    }

    private void shouldSaveNewUser() throws Exception {
        User user = utils.getUserWithId(1L);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(user)));
    }

    private void shouldReplaceNameIfItIsBlank() throws Exception {
        User user = utils.getUserWithBlankName();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ryan_Gosling"));
    }

    @Test
    public void validateUserModelTests() throws Exception {
        shouldReturnErrorMessageWhenEmailIsBlank();
        shouldReturnErrorMessageWhenEmailIsInvalid();
        shouldReturnErrorMessageWhenLoginIsBlank();
        shouldReturnErrorMessageWhenLoginHasSpaces();
        shouldReturnErrorMessageWhenBirthdayAfterCurrentDate();

    }

    private void shouldReturnErrorMessageWhenEmailIsBlank() throws Exception {
        User user = utils.getUserWithNullEmail();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Email не может быть пустым"));
    }

    private void shouldReturnErrorMessageWhenEmailIsInvalid() throws Exception {
        User user = utils.getUserWithInvalidEmail();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Неверный формат email"));
    }

    private void shouldReturnErrorMessageWhenLoginIsBlank() throws Exception {
        User user = utils.getUserWithBlankLogin();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Логин не может быть пустым"));
    }

    private void shouldReturnErrorMessageWhenLoginHasSpaces() throws Exception {
        User user = utils.getUserWithInvalidLogin();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Логин не может содержать пробелы"));
    }

    private void shouldReturnErrorMessageWhenBirthdayAfterCurrentDate() throws Exception {
        User user = utils.getUserWithBirthDateInFuture();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("День рождения не может быть в будущем"));
    }

    @Test
    public void updateUser() throws Exception {
        shouldUpdateUser();
        shouldReturnErrorMessageWhenIdIsNull();
        shouldReturnErrorMessageWhenIdDoesNotExist();

    }

    private void shouldUpdateUser() throws Exception {
        User user = utils.getUserWithId(1L);
        User updatedUser = utils.getUpdatedUser(user);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedUser)));
    }

    private void shouldReturnErrorMessageWhenIdIsNull() throws Exception {
        User updatedUser = utils.getUserWithId(null);

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("id не  может быть пустым"));
    }

    private void shouldReturnErrorMessageWhenIdDoesNotExist() throws Exception {
        User updatedUser = utils.getUserWithId(2L);

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Пользователя  с id: " + updatedUser.getId() + " не существует"));
    }
}
