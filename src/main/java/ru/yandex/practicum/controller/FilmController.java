package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.DTO.film.FilmRequestDTO;
import ru.yandex.practicum.DTO.film.FilmResponseDTO;
import ru.yandex.practicum.DTO.film.FilmUpdateDTO;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.service.FilmService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public ResponseEntity<List<FilmResponseDTO>> getAllFilms() {
        List<FilmResponseDTO> films = filmService.getAllFilms().stream()
                .map(this::toFilmResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(films);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilmResponseDTO> getFilmById(@PathVariable @NotNull @Positive int id) {
        Film film = filmService.getFilmById(id).orElse(null);
        return ResponseEntity.ok(toFilmResponseDTO(film));
    }

    @PostMapping
    public ResponseEntity<FilmResponseDTO> createFilm(@Valid @RequestBody FilmRequestDTO filmRequestDto) {
        Film film = filmService.createFilm(toFilm(filmRequestDto));
        return ResponseEntity.ok(toFilmResponseDTO(film));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Film> updateFilm(@PathVariable @NotNull @Positive Integer id,
                                           @RequestBody @Valid FilmUpdateDTO filmUpdateDTO) {
        Film existingFilm = filmService.getFilmById(id)
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

        Film updatedFilm = filmService.updateFilm(existingFilm);

        return ResponseEntity.ok(updatedFilm);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFilm(@PathVariable @NotNull @Positive Integer id) {
        filmService.deleteFilm(id);
        return ResponseEntity.noContent().build();
    }

    private Film toFilm(FilmRequestDTO filmRequestDto) {
        Film film = new Film();
        film.setName(filmRequestDto.getName());
        film.setDescription(filmRequestDto.getDescription());
        film.setReleaseDate(filmRequestDto.getReleaseDate());
        film.setGenres(filmRequestDto.getGenres());
        return film;
    }

    private FilmResponseDTO toFilmResponseDTO(Film film) {
        FilmResponseDTO fr = new FilmResponseDTO();
        fr.setId(film.getId());
        fr.setName(film.getName());
        fr.setDescription(film.getDescription());
        fr.setReleaseDate(film.getReleaseDate());
        fr.setGenres(film.getGenres());
        return fr;
    }
}
