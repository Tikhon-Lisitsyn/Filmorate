package ru.yandex.practicum.storage;

import ru.yandex.practicum.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    List<User> findAll();

    User create(User user);

    User update(User user);

    Optional<User> findUserById(Integer id);

    void delete(Integer id);
}
