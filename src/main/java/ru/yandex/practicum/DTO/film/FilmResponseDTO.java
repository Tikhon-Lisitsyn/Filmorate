package ru.yandex.practicum.DTO.film;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class FilmResponseDTO {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private List<Integer> genres;
}
