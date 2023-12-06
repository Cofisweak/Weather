package com.cofisweak.dao;

import com.cofisweak.exception.UsernameAlreadyExistsException;
import com.cofisweak.model.QUser;
import com.cofisweak.model.User;
import com.cofisweak.util.HibernateUtil;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.PersistenceException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDao {
    private static final UserDao INSTANCE = new UserDao();

    public void persist(User user) throws UsernameAlreadyExistsException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        } catch (PersistenceException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new UsernameAlreadyExistsException();
            }
        }
    }

    public Optional<User> getUserByLogin(String login) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = new JPAQuery<User>(session)
                    .select(QUser.user)
                    .from(QUser.user)
                    .where(QUser.user.login.eq(login))
                    .fetchOne();
            return Optional.ofNullable(user);
        }
    }

    public static UserDao getInstance() {
        return INSTANCE;
    }
}
