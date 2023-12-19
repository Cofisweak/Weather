package com.cofisweak.auth;

import com.cofisweak.exception.IncorrectPasswordException;
import com.cofisweak.exception.UserNotFoundException;
import com.cofisweak.exception.UsernameAlreadyExistsException;
import com.cofisweak.model.User;
import com.cofisweak.service.AuthService;
import com.cofisweak.util.HibernateUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AuthTest {

    final String EXIST_ACCOUNT_USERNAME = "exists";
    final String EXIST_ACCOUNT_PASSWORD = "exists";
    SessionFactory sessionFactory;
    AuthService authService;

    @BeforeEach
    void setUp() {
        sessionFactory = HibernateUtil.buildSessionFactory();
        authService = new AuthService(sessionFactory);

        authService.register(EXIST_ACCOUNT_USERNAME, EXIST_ACCOUNT_PASSWORD);
    }

    @Test
    void whenAccountCreatesThenAccountExists() {
        String login = "user";
        String password = "password";

        User createdUser = authService.register(login, password);

        User findUser = sessionFactory.openSession().find(User.class, createdUser.getId());
        Assertions.assertNotNull(findUser);
    }

    @Test
    void whenAccountCreatedWithExistingUsernameThenThrowsException() {
        Assertions.assertThrows(UsernameAlreadyExistsException.class,
                () -> authService.register(EXIST_ACCOUNT_USERNAME, EXIST_ACCOUNT_PASSWORD));
    }

    @Test
    void whenAccountExistsAndUserLoginThenSuccess() {
        Assertions.assertDoesNotThrow(
                () -> authService.login(EXIST_ACCOUNT_USERNAME, EXIST_ACCOUNT_PASSWORD));
    }

    @Test
    void whenAccountExistsAndUserLoginWithIncorrectPasswordThenThrowsException() {
        Assertions.assertThrows(IncorrectPasswordException.class,
                () -> authService.login(EXIST_ACCOUNT_USERNAME, "incorrect password"));
    }

    @Test
    void whenAccountDoesNotExistsAndUserLoginThenThrowsException() {
        Assertions.assertThrows(UserNotFoundException.class,
                () -> authService.login("random username", "random password"));
    }
}
