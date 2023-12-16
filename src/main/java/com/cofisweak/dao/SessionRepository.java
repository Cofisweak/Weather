package com.cofisweak.dao;

import com.cofisweak.model.Session;

import javax.persistence.Query;
import java.util.UUID;

public class SessionRepository extends BaseRepository<UUID, Session> {
    public SessionRepository() {
        super(Session.class);
    }

    public void removeExpiredSessions() {
        try (org.hibernate.Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String hql = "DELETE FROM Session WHERE expiresAt < now()";
            Query query = session.createQuery(hql);
            query.executeUpdate();
            session.getTransaction().commit();

        }
    }
}
