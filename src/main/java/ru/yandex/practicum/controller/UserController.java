package ru.yandex.practicum.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.User;

import java.time.Instant;
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
    public User create(@RequestBody User user) {
        if (validate(user)) {
            user.setId((int)getNextId());
            users.put(user.getId(), user);
        }
        log.info("В список занесён новый пользователь");
        return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        if (newUser.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }
        if (users.containsKey(newUser.getId())) {
            User existingUser = users.get(newUser.getId());
            if (newUser.getName() != null) {
                if (newUser.getLogin() != null) {
                    if (newUser.getLogin().isBlank() || newUser.getLogin().contains(" ")) {
                        throw new ValidationException("Логин не может быть пустым и содержать пробелы");
                    }
                    existingUser.setName(newUser.getLogin());
                }
                if (newUser.getName().isBlank()) {
                    existingUser.setName(existingUser.getLogin());
                }
            }
            if (newUser.getEmail() != null) {
                if (newUser.getEmail().isBlank() || !newUser.getEmail().contains("@")) {
                    throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
                }
                existingUser.setEmail(newUser.getEmail());
            }
            if (newUser.getLogin() != null) {
                if (newUser.getLogin().isBlank() || newUser.getLogin().contains(" ")) {
                    throw new ValidationException("Логин не может быть пустым и содержать пробелы");
                }
                existingUser.setLogin(newUser.getLogin());
            }
            if (newUser.getBirthday() != null) {
                if (newUser.getBirthday().isAfter(Instant.now())) {
                    throw new ValidationException("Продолжительность фильма должна быть положительным числом");
                }
                existingUser.setBirthday(newUser.getBirthday());
            }
            log.debug("Изменён пользователь в списке");
            return existingUser;
        }
        throw new ValidationException("Пользователь с id " + newUser.getId() + "не найден");
    }

    public boolean validate(User user) {
        if (!user.getEmail().contains("@") || user.getEmail().isBlank()) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday().isAfter(Instant.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return true;
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
