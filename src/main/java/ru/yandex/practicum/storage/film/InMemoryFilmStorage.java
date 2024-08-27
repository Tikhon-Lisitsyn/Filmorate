package ru.yandex.practicum.storage.film;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.DTO.FilmUpdateDTO;
import ru.yandex.practicum.controller.FilmController;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);
    @Getter
    Map<Integer, Film> films = new HashMap<>();
    private final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    public Collection<Film> findAll() {
        log.info("Пользователь запросил список фильмов");
        return films.values();
    }

    public Film create(Film film) {
        film.setId((int) getNextId());
        films.put(film.getId(), film);

        log.info("Пользователь занёс в список новый фильм");
        return film;
    }

    public Film update(FilmUpdateDTO newFilm) {
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

    public long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
