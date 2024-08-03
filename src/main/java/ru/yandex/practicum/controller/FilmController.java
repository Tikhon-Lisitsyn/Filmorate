package ru.yandex.practicum.controller;

import jakarta.validation.Constraint;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.DTO.FilmUpdateDTO;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);
    Map<Integer, Film> films = new HashMap<>();
    private final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895,12,28);

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Пользователь запросил список фильмов");
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        film.setId((int) getNextId());
        films.put(film.getId(), film);

        log.info("Пользователь занёс в список новый фильм");
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody FilmUpdateDTO newFilm) {
        if (newFilm.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }
        if (!films.containsKey(newFilm.getId())) {
            throw new ValidationException("Фильм с id " + newFilm.getId() + "не найден");
        }

        Film existingFilm = films.get(newFilm.getId());
        if (newFilm.getName() != null) {
            existingFilm.setName(newFilm.getName());
        }
        if (newFilm.getDescription() != null) {
            existingFilm.setDescription(newFilm.getDescription());
        }
        if (newFilm.getReleaseDate() != null) {
            if (newFilm.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
                throw new ValidationException("Дата релиза не может быть раньше, чем 28 декабря 1895 года");
            }
            existingFilm.setReleaseDate(newFilm.getReleaseDate());
        }
        if (newFilm.getDuration() != null) {
            existingFilm.setDuration(newFilm.getDuration());
        }
        log.debug("Пользователь изменил фильм в списке");
        return existingFilm;
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}