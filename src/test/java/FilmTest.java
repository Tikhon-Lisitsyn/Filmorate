import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.controller.FilmController;
import ru.yandex.practicum.controller.UserController;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.Film;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.model.User;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class FilmTest {
    Film film;
    FilmController filmController;

    @BeforeEach
    void setup() {
        film = new Film();
        filmController = new FilmController();
    }

    @Test
    void film1AndFilm2Equals() {
        Film film1 = new Film();
        film1.setId(1);
        film1.setDescription("test_description");
        film1.setReleaseDate(Instant.now());
        film1.setName("test name");
        film1.setDuration(3);

        Film film2 = new Film();
        film2.setId(1);
        film2.setDescription("test_description");
        film2.setReleaseDate(Instant.now());
        film2.setName("test name");
        film2.setDuration(3);

        assertEquals(film1,film2,"Два фильма не равны!");
    }

    @Test
    void filmNameFailValidation() throws ValidationException {
        film.setName("");

        assertThrows(ValidationException.class, () -> {
           filmController.validate(film);
        });
    }

    @Test
    void filmDescriptionFailValidation() throws ValidationException {
        film.setName("test_name");
        film.setDescription("description of more than 200 characters description of more than 200 characters " +
                "description of more than 200 characters description of more than 200 characters" +
                " description of more than 200 characters...");

        assertThrows(ValidationException.class, () -> {
            filmController.validate(film);
        });
    }

    @Test
    void filmReleaseDateFailValidation() throws ValidationException {
        film.setName("test_name");
        film.setDescription("test_description");
        film.setReleaseDate(Instant.ofEpochMilli(-2335564801L));

        assertThrows(ValidationException.class, () -> {
            filmController.validate(film);
        });
    }

    @Test
    void filmDurationFailValidation() throws ValidationException {
        film.setName("test_name");
        film.setDescription("test_description");
        film.setReleaseDate(Instant.ofEpochMilli(0));
        film.setDuration(-1);

        assertThrows(ValidationException.class, () -> {
            filmController.validate(film);
        });
    }
}
