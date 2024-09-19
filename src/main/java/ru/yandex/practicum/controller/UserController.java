package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.DTO.user.UserRequestDTO;
import ru.yandex.practicum.DTO.user.UserResponseDTO;
import ru.yandex.practicum.DTO.user.UserUpdateDTO;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers().stream()
                .map(this::toUserResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable @NotNull @Positive int id) {
        User user = userService.getUserById(id).orElse(null);
        return ResponseEntity.ok(toUserResponseDto(user));
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid UserRequestDTO userRequestDto) {
        User user = userService.createUser(toUser(userRequestDto));
        return ResponseEntity.ok(toUserResponseDto(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable @NotNull @Positive Integer id,
                                           @RequestBody @Valid UserUpdateDTO userUpdateDTO) {
        User existingUser = userService.getUserById(id)
                .orElseThrow(() -> new NotFoundException("User with ID " + id + " not found"));

        existingUser.setName(userUpdateDTO.getName() != null ? userUpdateDTO.getName() : existingUser.getName());
        existingUser.setEmail(userUpdateDTO.getEmail() != null ? userUpdateDTO.getEmail() : existingUser.getEmail());
        existingUser.setLogin(userUpdateDTO.getLogin() != null ? userUpdateDTO.getLogin() : existingUser.getLogin());
        existingUser.setBirthday(userUpdateDTO.getBirthday() != null ? userUpdateDTO.getBirthday() : existingUser.getBirthday());

        User updatedUser = userService.updateUser(existingUser);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable @NotNull @Positive Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    private UserResponseDTO toUserResponseDto(User user) {
        UserResponseDTO ur = new UserResponseDTO();
        ur.setId(user.getId());
        ur.setEmail(user.getEmail());
        ur.setLogin(user.getLogin());
        ur.setName(user.getName());
        ur.setBirthday(user.getBirthday());
        return ur;
    }

    private User toUser(UserRequestDTO userRequestDto) {
        User user = new User();
        user.setEmail(userRequestDto.getEmail());
        user.setLogin(userRequestDto.getLogin());
        user.setName(userRequestDto.getName());
        user.setBirthday(userRequestDto.getBirthday());
        return user;
    }
}
