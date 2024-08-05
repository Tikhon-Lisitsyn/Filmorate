package ru.yandex.practicum.model;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Data
public class Film {
    private Integer id;

    @NotNull(message = "Название должно быть указано")
    @NotBlank(message = "Название не может быть пустым")
    private String name;

    @NotNull(message = "Описание должно быть указано")
    @Size(max = 200, message = "Максимальная длина описания - 200 символов")
    private String description;

    @NotNull(message = "Дата релиза должна быть указана")
    @PastOrPresent(message = "Дата релиза не может быть раньше, чем 28 декабря 1895 года")
    private LocalDate releaseDate;

    @NotNull(message = "Продолжительность должна быть указана")
    @Positive(message = "Продолжительность фильма должна быть положительным числом")
    private Integer duration;
}