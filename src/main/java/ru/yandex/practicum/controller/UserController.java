package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.DTO.UserUpdateDTO;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final static Logger log = LoggerFactory.getLogger(UserController.class);
    Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        log.info("Запрошен список пользователей");
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        user.setId((int)getNextId());
        users.put(user.getId(), user);

        log.info("В список занесён новый пользователь");
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody UserUpdateDTO newUser) {
        if (newUser.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }
        if (!users.containsKey(newUser.getId())) {
            throw new ValidationException("Пользователь с id " + newUser.getId() + "не найден");
        }

        User existingUser = users.get(newUser.getId());
        if (newUser.getName() != null) {
            if (newUser.getLogin() != null) {
                existingUser.setName(newUser.getLogin());
            }
            if (newUser.getName().isBlank()) {
                existingUser.setName(existingUser.getLogin());
            }
        }
        if (newUser.getEmail() != null) {
            existingUser.setEmail(newUser.getEmail());
        }
        if (newUser.getLogin() != null) {
            existingUser.setLogin(newUser.getLogin());
        }
        if (newUser.getBirthday() != null) {
            existingUser.setBirthday(newUser.getBirthday());
        }

        log.debug("Изменён пользователь в списке");
        return existingUser;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}