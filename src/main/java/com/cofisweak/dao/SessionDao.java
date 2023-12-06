package com.cofisweak.dao;

import com.cofisweak.model.Session;
import com.cofisweak.util.HibernateUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SessionDao {
    private static final SessionDao INSTANCE = new SessionDao();

    public static SessionDao getInstance() {
        return INSTANCE;
    }

    public Session save(Session session) {
        try (org.hibernate.Session entityManager = HibernateUtil.getSessionFactory().openSession()) {
            entityManager.beginTransaction();
            entityManager.persist(session);
            entityManager.getTransaction().commit();
        }
        return session;
    }

    public void remove(Session session) {
        try (org.hibernate.Session entityManager = HibernateUtil.getSessionFactory().openSession()) {
            entityManager.beginTransaction();
            entityManager.remove(session);
            entityManager.getTransaction().commit();
        }
    }

    public Optional<Session> get(UUID uuid) {
        try (org.hibernate.Session entityManager = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(entityManager.get(Session.class, uuid));
        }
    }
}
