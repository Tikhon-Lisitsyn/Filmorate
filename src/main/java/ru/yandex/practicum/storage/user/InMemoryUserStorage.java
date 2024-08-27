package ru.yandex.practicum.storage.user;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.DTO.UserUpdateDTO;
import ru.yandex.practicum.controller.UserController;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final static Logger log = LoggerFactory.getLogger(UserController.class);
    @Getter
    Map<Integer, User> users = new HashMap<>();

    public Collection<User> findAll() {
        log.info("Запрошен список пользователей");
        return users.values();
    }

    public User create(User user) {
        user.setId((int) getNextId());
        users.put(user.getId(), user);

        log.info("В список занесён новый пользователь");
        return user;
    }

    public User update(UserUpdateDTO newUser) {
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

    public long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
