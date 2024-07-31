package ru.yandex.practicum.model;

import java.time.Instant;
import lombok.Data;

@Data
public class User {
    private Integer id;
    private String email;
    private String login;
    private String name;
    private Instant birthday;
}
