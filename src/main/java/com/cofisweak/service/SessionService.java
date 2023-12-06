package com.cofisweak.service;

import com.cofisweak.dao.SessionDao;
import com.cofisweak.model.Session;
import com.cofisweak.model.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SessionService {
    private static final SessionService INSTANCE = new SessionService();
    private final SessionDao sessionDao = SessionDao.getInstance();

    public Session createSession(User user) {
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(1);
        UUID uuid = UUID.randomUUID();
        Session session = Session.builder()
                .id(uuid)
                .user(user)
                .expiresAt(expiresAt)
                .build();
        return sessionDao.save(session);
    }

    public void removeSession(Session session) {
        sessionDao.remove(session);
    }

    public static SessionService getInstance() {
        return INSTANCE;
    }

    public Optional<Session> getSession(UUID uuid) {
        return sessionDao.get(uuid);
    }
}
