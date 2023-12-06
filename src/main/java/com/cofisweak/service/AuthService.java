package com.cofisweak.service;

import com.cofisweak.dao.UserDao;
import com.cofisweak.exception.IncorrectPasswordException;
import com.cofisweak.exception.UserNotFoundException;
import com.cofisweak.exception.UsernameAlreadyExistsException;
import com.cofisweak.model.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.hibernate.exception.ConstraintViolationException;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthService {
    private static final AuthService INSTANCE = new AuthService();
    private final UserDao userDao = UserDao.getInstance();

    public User login(String username, String password) throws IncorrectPasswordException, UserNotFoundException {
        Optional<User> user = userDao.getUserByLogin(username);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        String hashedPassword = user.get().getPassword();
        if (!BCrypt.checkpw(password, hashedPassword)) {
            throw new IncorrectPasswordException();
        }
        return user.get();
    }

    @SneakyThrows
    public User register(String username, String password) throws UsernameAlreadyExistsException {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = User.builder()
                .login(username)
                .password(hashedPassword)
                .build();
        try {
            userDao.persist(user);
        } catch (ConstraintViolationException e) {
            throw new UsernameAlreadyExistsException();
        }
        return user;
    }

    public static AuthService getInstance() {
        return INSTANCE;
    }
}
