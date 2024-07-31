package ru.yandex.practicum.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.Film;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);
    Map<Integer, Film> films = new HashMap<>();
    private final int CHARACTER_LIMIT = 200;
    private final Instant MIN_RELEASE_DATE = Instant.ofEpochMilli(-2335564800L);

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Пользователь запросил список фильмов");
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        if (validate(film)) {
            film.setId((int)getNextId());
            films.put(film.getId(), film);
        }
        log.info("Пользователь занёс в список новый фильм");
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        if (newFilm.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }
        if (films.containsKey(newFilm.getId())) {
            Film existingFilm = films.get(newFilm.getId());
            if (newFilm.getName() != null) {
                if (newFilm.getName().isBlank()) {
                    throw new ValidationException("Название не может быть пустым");
                }
                existingFilm.setName(newFilm.getName());
            }
            if (newFilm.getDescription() != null) {
                if (newFilm.getDescription().length() > CHARACTER_LIMIT) {
                    throw new ValidationException("Максимальная длина описания - 200 символов");
                }
                existingFilm.setDescription(newFilm.getDescription());
            }
            if (newFilm.getReleaseDate() != null) {
                if (newFilm.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
                    throw new ValidationException("Дата релиза не может быть раньше, чем 28 декабря 1895 года");
                }
                existingFilm.setReleaseDate(newFilm.getReleaseDate());
            }
            if (newFilm.getDuration() != null) {
                if (newFilm.getDuration() <= 0) {
                    throw new ValidationException("Продолжительность фильма должна быть положительным числом");
                }
                existingFilm.setDuration(newFilm.getDuration());
            }
            log.debug("Пользователь изменил фильм в списке");
            return existingFilm;
        }
        throw new ValidationException("Фильм с id " + newFilm.getId() + "не найден");
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public boolean validate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > CHARACTER_LIMIT) {
            throw new ValidationException("Максимальная длина описания - 200 символов");
        }
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            throw new ValidationException("Дата релиза не может быть раньше, чем 28 декабря 1895 года");
        }
        if (film.getDuration()<=0) {
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
        log.debug("Фильм прошёл проверку");
        return true;
    }
}
