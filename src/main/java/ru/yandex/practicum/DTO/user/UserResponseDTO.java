package ru.yandex.practicum.DTO.user;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserResponseDTO {
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private List<UserResponseDTO> friends;
}
