package ru.yandex.practicum.filmorate.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.validation.constraints.*;


import lombok.Data;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
public class Film {

    public Film(String name, String description, LocalDate releaseDate, Integer duration) {
        this.name = name;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.description = description;
        this.usersWhoLiked = new HashSet<>();
    }

    private Long id;
    @NotNull
    @NotBlank(message = "Название не может быть пустым")
    private String name;
    @Size(max = 200, message = "Длина описания не может превышать 200 символов")
    private String description;

    private LocalDate releaseDate;
    @Min(value = 0, message = "Длительность фильма не может быть отрицательным числом")
    private Integer duration;
    @JsonBackReference
    private Set<User> usersWhoLiked;
}
