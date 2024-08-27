package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.DTO.UserUpdateDTO;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.service.UserService;
import ru.yandex.practicum.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody UserUpdateDTO newUser) {
        return userService.update(newUser);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable(value = "id", required = false) Integer userId,
                          @PathVariable(required = false) Integer friendId) {
        return userService.addFriend(userId,friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable(value = "id", required = false) Integer userId,
                             @PathVariable(required = false) Integer friendId) {
        return userService.deleteFriend(userId,friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable(value = "id", required = false) Integer userId) {
        return userService.getFriends(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getMutualFriends(@PathVariable(value = "id", required = false) Integer userId,
                                             @PathVariable(value = "otherId", required = false ) Integer friendId) {
        return userService.getMutualFriends(userId, friendId);
    }
}