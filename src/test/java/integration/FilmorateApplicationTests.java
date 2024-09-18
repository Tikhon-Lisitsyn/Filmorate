package integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import ru.yandex.practicum.storage.user.UserDbStorage;
import ru.yandex.practicum.model.User;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class FilmorateApplicationTests {

    @Autowired
    private UserDbStorage userDbStorage;

    @Test
    void testFindUserById() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setLogin("testUser");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        userDbStorage.create(user);

        Optional<User> userOptional = userDbStorage.findUserById(1);
        assertThat(userOptional).isPresent();
        assertThat(userOptional.get()).hasFieldOrPropertyWithValue("id", 1);
    }
}
