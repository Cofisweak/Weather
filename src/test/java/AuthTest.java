import com.cofisweak.exception.IncorrectPasswordException;
import com.cofisweak.exception.UserNotFoundException;
import com.cofisweak.exception.UsernameAlreadyExistsException;
import com.cofisweak.model.User;
import com.cofisweak.service.AuthService;
import com.cofisweak.util.HibernateUtil;
import org.hibernate.Session;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthTest {

    private final AuthService authService = AuthService.getInstance();

    @Test
    @Order(1)
    void testRegister() throws UsernameAlreadyExistsException {
        String username = "username";
        String password = "password";
        User createdUser = authService.register(username, password);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.get(User.class, createdUser.getId());
            Assertions.assertEquals(username, user.getLogin());
            Assertions.assertEquals(1L, user.getId());
        }
    }

    @Test
    @Order(2)
    void testRegisterWithNoUniqueUsername() {
        String username = "username";
        String password = "password";
        Assertions.assertThrows(
                UsernameAlreadyExistsException.class,
                () -> authService.register(username, password));
    }

    @Test
    void testLoginAfterRegister() throws UserNotFoundException, IncorrectPasswordException {
        String username = "username";
        String password = "password";
        User createdUser = authService.login(username, password);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.get(User.class, createdUser.getId());
            Assertions.assertEquals(username, user.getLogin());
            Assertions.assertEquals(1L, user.getId());
        }
    }

    @Test
    void testLoginWithIncorrectPassword() {
        String username = "username";
        String password = "incorrectPassword";
        Assertions.assertThrows(
                IncorrectPasswordException.class,
                () -> authService.login(username, password));
    }

    @Test
    void testLoginWithIncorrectUsername() {
        String username = "incorrectUsername";
        String password = "password";
        Assertions.assertThrows(
                UserNotFoundException.class,
                () -> authService.login(username, password));
    }
}
