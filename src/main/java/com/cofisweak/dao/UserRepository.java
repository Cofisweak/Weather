package com.cofisweak.dao;

import com.cofisweak.exception.UsernameAlreadyExistsException;
import com.cofisweak.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;

import javax.persistence.PersistenceException;
import java.util.Optional;

public class UserRepository extends BaseRepository<Long, User> {
    public UserRepository(SessionFactory sessionFactory) {
        super(sessionFactory, User.class);
    }

    @Override
    public User save(User object) throws UsernameAlreadyExistsException {
        try {
            return super.save(object);
        } catch (PersistenceException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new UsernameAlreadyExistsException();
            }
        }
        return object;
    }

    public Optional<User> getUserByLogin(String login) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM User where login = :login";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("login", login);
            return query.uniqueResultOptional();
        }
    }
}
