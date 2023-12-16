package com.cofisweak.service;

import com.cofisweak.dao.SessionRepository;
import com.cofisweak.model.Session;
import com.cofisweak.model.User;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class SessionService {
    private final SessionRepository sessionRepository = new SessionRepository();

    public Session createSession(User user) {
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(1);
        UUID uuid = UUID.randomUUID();
        Session session = Session.builder()
                .id(uuid)
                .user(user)
                .expiresAt(expiresAt)
                .build();
        return sessionRepository.save(session);
    }

    public void removeSession(Session session) {
        sessionRepository.delete(session);
    }

    public Optional<Session> getSession(UUID uuid) {
        return sessionRepository.getById(uuid);
    }

    public void removeExpiredSessions() {
        sessionRepository.removeExpiredSessions();
    }
}
