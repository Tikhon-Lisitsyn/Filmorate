package ru.yandex.practicum.model;

import java.time.Duration;
import java.time.Instant;
import lombok.Data;

@Data
public class Film {
    private Integer id;
    private String name;
    private String description;
    private Instant releaseDate;
    private Integer duration;
}
