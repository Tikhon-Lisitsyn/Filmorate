package ru.yandex.practicum.storage.user;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.DTO.UserUpdateDTO;
import ru.yandex.practicum.model.User;

import java.util.Collection;

public interface UserStorage {
    public Collection<User> findAll();
    public User create(User user);
    public User update(UserUpdateDTO newUser);
    public long getNextId();
}
