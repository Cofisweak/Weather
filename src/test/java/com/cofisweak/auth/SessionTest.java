package com.cofisweak.auth;

import com.cofisweak.model.Session;
import com.cofisweak.model.User;
import com.cofisweak.service.AuthService;
import com.cofisweak.service.SessionService;
import com.cofisweak.util.HibernateUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class SessionTest {

    SessionFactory sessionFactory;
    SessionService sessionService;

    User existingUser;
    Session existingSession;

    @BeforeEach
    void setUp() {
        sessionFactory = HibernateUtil.buildSessionFactory();
        sessionService = new SessionService(sessionFactory);

        AuthService authService = new AuthService(sessionFactory);

        existingUser = authService.register("user", "password");
        existingSession = sessionService.createSession(existingUser);
    }

    @Test
    void whenSessionCreatesThenSessionExists() {
        Session session = sessionService.createSession(existingUser);

        Session findSession = sessionFactory.openSession().find(Session.class, session.getId());
        Assertions.assertNotNull(findSession);
    }

    @Test
    void whenGetNonExistentSessionThenOptionalIsEmpty() {
        UUID uuid = UUID.randomUUID();
        Optional<Session> session = sessionService.getSession(uuid);
        Assertions.assertTrue(session.isEmpty());
    }

    @Test
    void whenGetExistingSessionThenSessionExists() {
        Optional<Session> session = sessionService.getSession(existingSession.getId());
        Assertions.assertTrue(session.isPresent());
    }

    @Test
    void whenRemoveSessionThenSessionDoesNotExist() {
        sessionService.removeSession(existingSession);

        Optional<Session> session = sessionService.getSession(existingSession.getId());
        Assertions.assertTrue(session.isEmpty());
    }

    @Test
    void whenRemoveExpiredSessionThenExpiredSessionRemovesFromDatabase() {
        Session createdExpiredSession = sessionService.createSession(existingUser);
        try (org.hibernate.Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Session expiredSession = session.get(Session.class, createdExpiredSession.getId());
            expiredSession.setExpiresAt(LocalDateTime.now().minusDays(1));
            session.update(expiredSession);
            session.getTransaction().commit();
        }

        sessionService.removeExpiredSessions();

        Optional<Session> session = sessionService.getSession(existingSession.getId());
        Optional<Session> nonExistentExpiredSession = sessionService.getSession(createdExpiredSession.getId());
        Assertions.assertTrue(session.isPresent());
        Assertions.assertTrue(nonExistentExpiredSession.isEmpty());
    }
}
