package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.DTO.UserUpdateDTO;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.user.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final InMemoryUserStorage inMemoryUserStorage;
    private final static Logger log = LoggerFactory.getLogger(UserService.class);

    public Collection<User> findAll() {
        return inMemoryUserStorage.findAll();
    }

    public User create(User user) {
        return inMemoryUserStorage.create(user);
    }

    public User update(UserUpdateDTO newUser) {
        return inMemoryUserStorage.update(newUser);
    }

    public User addFriend(Integer addingUserId, Integer addableUserId) {
        validate(addingUserId,addableUserId);
        inMemoryUserStorage.getUsers().get(addingUserId).getFriends().add(addableUserId);
        inMemoryUserStorage.getUsers().get(addableUserId).getFriends().add(addingUserId);
        log.debug("Пользователь добавил другого пользователя в друзья");
        return inMemoryUserStorage.getUsers().get(addableUserId);
    }

    public User deleteFriend(Integer removingUserId, Integer removableUserId) {
        validate(removingUserId,removableUserId);
        if (!inMemoryUserStorage.getUsers().get(removingUserId).getFriends().contains(removableUserId)) {
            throw new ValidationException("Эти пользователи не являются друзьями");
        }
        inMemoryUserStorage.getUsers().get(removingUserId).getFriends().remove(removableUserId);
        inMemoryUserStorage.getUsers().get(removableUserId).getFriends().remove(removingUserId);
        log.debug("Пользователь удалил другого пользователя из друзей");
        return inMemoryUserStorage.getUsers().get(removableUserId);
    }

    public Collection<User> getFriends(Integer userId) {
        if (userId == null) {
            throw new ValidationException("ID пользователя не может быть равным null");
        }
        if (!inMemoryUserStorage.getUsers().containsKey(userId)) {
            throw new NotFoundException("Не найден пользователь с ID = "+userId);
        }
        List<User> userFriends = new ArrayList<>();
        for (Integer id : inMemoryUserStorage.getUsers().get(userId).getFriends()) {
            userFriends.add(inMemoryUserStorage.getUsers().get(id));
        }
        return userFriends;
    }

    public Collection<User> getMutualFriends(Integer userId1, Integer userId2) {
        validate(userId1,userId2);
        List<User> mutualFriends = new ArrayList<>();
        for (Integer user: inMemoryUserStorage.getUsers().get(userId1).getFriends()) {
            if (inMemoryUserStorage.getUsers().get(userId2).getFriends().contains(user)) {
                mutualFriends.add(inMemoryUserStorage.getUsers().get(user));
            }
        }
        log.debug("Пользователь запросил список общих друзей");
        return mutualFriends;
    }

    public void validate(Integer id1, Integer id2) {
        log.info("ID пользователей проходят проверку");
        if (id1 == null || id2 == null) {
            throw new ValidationException("ID пользователей не могут быть равны null");
        }
        if (id1.equals(id2)) {
            throw new ValidationException("ID пользователей должны быть разные");
        }
        if (!inMemoryUserStorage.getUsers().containsKey(id1)) {
            throw new NotFoundException("Не найден пользователь с ID = "+id1);
        }
        if (!inMemoryUserStorage.getUsers().containsKey(id2)) {
            throw new NotFoundException("Не найден пользователь с ID = "+id2);
        }
    }
}
