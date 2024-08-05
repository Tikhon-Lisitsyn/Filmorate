import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import ru.yandex.practicum.model.Film;

import java.time.LocalDate;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmTest {

    private final Validator validator;

    public FilmTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testFilmValidation() {
        Film film = new Film();
        film.setName("");
        film.setDescription("A".repeat(201));
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        film.setDuration(-1);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(3, violations.size());
    }

    @Test
    public void testValidFilm() {
        Film film = new Film();
        film.setName("Valid Film");
        film.setDescription("A valid description");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(120);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(0, violations.size());
    }
}