import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.controller.UserController;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.User;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    User user;
    UserController userController;

    @BeforeEach
    void setup() {
        user = new User();
        userController = new UserController();
    }

    @Test
    void user1AndUser2Equals() {
        User user1 = new User();
        user1.setId(1);
        user1.setLogin("test_login");
        user1.setEmail("email@test.com");
        user1.setName("test name");
        user1.setBirthday(Instant.ofEpochMilli(2));

        User user2 = new User();
        user2.setId(1);
        user2.setLogin("test_login");
        user2.setEmail("email@test.com");
        user2.setName("test name");
        user2.setBirthday(Instant.ofEpochMilli(2));

        assertEquals(user1,user2,"Пользователи не равны!");
    }

    @Test
    void userEmailFailValidation() throws ValidationException {
        user.setEmail("email without special symbol");

        assertThrows(ValidationException.class, () -> {
            userController.validate(user);
        });
    }

    @Test
    void userLoginFailValidation() throws ValidationException {
        user.setLogin("login with space");
        user.setName("test_name");
        user.setBirthday(Instant.now().minusSeconds(3));
        user.setEmail("email@test.com");

        assertThrows(ValidationException.class, () -> {
            userController.validate(user);
        });
    }

    @Test
    void userNameEqualsLogin() {
        user.setLogin("TestLogin");
        user.setName("");
        user.setBirthday(Instant.now().minusSeconds(3));
        user.setEmail("email@test.com");
        userController.validate(user);

        assertEquals(user.getLogin(),user.getName());
    }

    @Test
    void userBirthdayFailValidation() throws ValidationException {
        user.setLogin("TestLogin");
        user.setName("test_name");
        user.setBirthday(Instant.now().plusSeconds(3));
        user.setEmail("email@test.com");

        assertThrows(ValidationException.class, () -> {
            userController.validate(user);
        });
    }
}
