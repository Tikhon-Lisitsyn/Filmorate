package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.storage.user.InMemoryUserStorage;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final InMemoryUserStorage inMemoryUserStorage;
    private final static Logger log = LoggerFactory.getLogger(FilmService.class);

    public Film addLike(Integer userId, Integer filmId) {
        validate(userId, filmId);
        if (inMemoryUserStorage.getUsers().get(userId).getLikes().contains(filmId)) {
            throw new ValidationException("Фильм с ID = " + filmId + " уже имеет оценку");
        }
        inMemoryUserStorage.getUsers().get(userId).getLikes().add(filmId);
        return inMemoryFilmStorage.getFilms().get(filmId);
    }

    public Film deleteLike(Integer userId, Integer filmId) {
        validate(userId, filmId);
        if (!inMemoryUserStorage.getUsers().get(userId).getLikes().contains(filmId)) {
            throw new ValidationException("Фильм с ID = " + filmId + " не оценён");
        }
        inMemoryUserStorage.getUsers().get(userId).getLikes().remove(filmId);
        return inMemoryFilmStorage.getFilms().get(filmId);
    }

    public Collection<Film> getTopFilms() {
        Map<Integer, Integer> likeCountMap = new HashMap<>();

        for (User user : inMemoryUserStorage.getUsers().values()) {
            for (Integer filmId : user.getLikes()) {
                likeCountMap.put(filmId, likeCountMap.getOrDefault(filmId, 0) + 1);
            }
        }
        List<Film> topFilms = new ArrayList<>(inMemoryFilmStorage.getFilms().values());

        topFilms.sort((a, b) -> likeCountMap.getOrDefault(b.getId(), 0)
                - likeCountMap.getOrDefault(a.getId(), 0));
        log.debug("Выводится топ-10 фильмов по количеству лайков");
        return topFilms;
    }

    public Collection<Film> getTopFilmsWithCount(Integer count) {
        if (count == null) {
            return getTopFilms();
        }
        if (count <= 0) {
            throw new ValidationException("Количество фильмов должно быть положительным числом");
        }

        List<Film> requiredFilms = new ArrayList<>();
        List<Film> topFilms = new ArrayList<>(getTopFilms());
        for (int i = 1; i<count;i++) {
            requiredFilms.add(topFilms.get(i));
        }
        return requiredFilms;
    }

    public void validate(Integer userId, Integer filmId) {
        log.info("ID фильмов проходят проверку");
        if (!inMemoryFilmStorage.getFilms().containsKey(filmId)) {
            throw new NotFoundException("Не найден фильм с ID = " + filmId);
        }
        if (!inMemoryUserStorage.getUsers().containsKey(userId)) {
            throw new NotFoundException("Не найден пользователь с ID = " + filmId);
        }
    }
}
