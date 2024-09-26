package ru.yandex.practicum.filmorate.model;


import jakarta.validation.constraints.*;


import lombok.Data;


import java.time.LocalDate;

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
}
