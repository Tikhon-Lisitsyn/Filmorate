package ru.yandex.practicum.storage.film;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.DTO.FilmUpdateDTO;
import ru.yandex.practicum.model.Film;

import java.util.Collection;

public interface FilmStorage {
    public Collection<Film> findAll();
    public Film create(Film film);
    public Film update(FilmUpdateDTO newFilm);
    public long getNextId();
}
