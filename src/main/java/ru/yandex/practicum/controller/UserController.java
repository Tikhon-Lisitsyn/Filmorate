package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.DTO.UserUpdateDTO;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.UserStorage;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserStorage userStorage;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userStorage.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        return userStorage.findUserById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFoundException("User with ID " + id + " not found"));
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userStorage.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody UserUpdateDTO userUpdateDTO) {
        User existingUser = userStorage.findUserById(id)
                .orElseThrow(() -> new NotFoundException("User with ID " + id + " not found"));

        existingUser.setName(userUpdateDTO.getName() != null ? userUpdateDTO.getName() : existingUser.getName());
        existingUser.setEmail(userUpdateDTO.getEmail() != null ? userUpdateDTO.getEmail() : existingUser.getEmail());
        existingUser.setLogin(userUpdateDTO.getLogin() != null ? userUpdateDTO.getLogin() : existingUser.getLogin());
        existingUser.setBirthday(userUpdateDTO.getBirthday() != null ? userUpdateDTO.getBirthday() : existingUser.getBirthday());

        User updatedUser = userStorage.update(existingUser);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userStorage.delete(id);
        return ResponseEntity.noContent().build();
    }
}
