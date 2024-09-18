package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.DTO.FilmUpdateDTO;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.storage.FilmStorage;

import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmStorage filmStorage;

    @GetMapping
    public ResponseEntity<List<Film>> getAllFilms() {
        List<Film> films = filmStorage.findAll();
        return ResponseEntity.ok(films);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable Integer id) {
        return filmStorage.findFilmById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFoundException("Film with ID " + id + " not found"));
    }

    @PostMapping
    public ResponseEntity<Film> createFilm(@RequestBody Film film) {
        Film createdFilm = filmStorage.create(film);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFilm);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Film> updateFilm(@PathVariable Integer id, @RequestBody FilmUpdateDTO filmUpdateDTO) {
        // Найти фильм по ID
        Film existingFilm = filmStorage.findFilmById(id)
                .orElseThrow(() -> new NotFoundException("Film with ID " + id + " not found"));

        if (filmUpdateDTO.getName() != null) {
            existingFilm.setName(filmUpdateDTO.getName());
        }
        if (filmUpdateDTO.getDescription() != null) {
            existingFilm.setDescription(filmUpdateDTO.getDescription());
        }
        if (filmUpdateDTO.getReleaseDate() != null) {
            existingFilm.setReleaseDate(filmUpdateDTO.getReleaseDate());
        }
        if (filmUpdateDTO.getDuration() != null) {
            existingFilm.setDuration(filmUpdateDTO.getDuration());
        }

        Film updatedFilm = filmStorage.update(existingFilm);

        return ResponseEntity.ok(updatedFilm);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFilm(@PathVariable Integer id) {
        filmStorage.delete(id);
        return ResponseEntity.noContent().build();
    }
}
