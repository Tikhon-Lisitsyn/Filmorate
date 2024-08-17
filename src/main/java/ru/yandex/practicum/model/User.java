package ru.yandex.practicum.model;

import java.time.LocalDate;
import java.util.Set;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class User {
    private Integer id;

    @NotNull(message = "Электронная почта должна быть указана")
    @NotBlank(message = "Электронная почта не может быть пустой")
    @Email(message = "Электронная почта должна содержать символ @")
    private String email;

    @NotNull(message = "Логин должен быть указан")
    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "^\\S+$", message = "Логин не может содержать пробелы")
    private String login;

    @NotNull(message = "Имя должно быть указано")
    private String name;

    @NotNull(message = "Дата рождения должна быть указана")
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;

    private Set<Integer> friends;
    private Set<Integer> likes;
}