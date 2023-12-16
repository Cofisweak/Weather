package com.cofisweak.dao;

import com.cofisweak.util.HibernateUtil;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class BaseRepository<K extends Serializable, E extends Serializable> {
    protected final SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
    protected final Class<E> clazz;

    public E save(E object) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(object);
            session.getTransaction().commit();
        }
        return object;
    }

    public void delete(E object) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(object);
            session.getTransaction().commit();
        }
    }

    public Optional<E> getById(K id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(clazz, id));
        }
    }
}
