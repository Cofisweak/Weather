import com.cofisweak.exception.UsernameAlreadyExistsException;
import com.cofisweak.model.Session;
import com.cofisweak.model.User;
import com.cofisweak.service.AuthService;
import com.cofisweak.service.SessionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

class SessionTest {
    private final SessionService sessionService = SessionService.getInstance();
    private static User user;

    @BeforeAll
    static void initUser() throws UsernameAlreadyExistsException {
        AuthService authService = AuthService.getInstance();
        user = authService.register("sessionTest", "sessionTest");
    }

    @Test
    void testCreateSession() {
        Session session = sessionService.createSession(user);
        LocalDateTime now = LocalDateTime.now();
        Assertions.assertTrue(now.isBefore(session.getExpiresAt()));
    }

    @Test
    void testGetSession() {
        Session createdSession = sessionService.createSession(user);
        Optional<Session> session = sessionService.getSession(createdSession.getId());
        Assertions.assertTrue(session.isPresent());
    }

    @Test
    void testRemoveSession() {
        Session createdSession = sessionService.createSession(user);
        sessionService.removeSession(createdSession);
        Optional<Session> session = sessionService.getSession(createdSession.getId());
        Assertions.assertTrue(session.isEmpty());
    }
}
