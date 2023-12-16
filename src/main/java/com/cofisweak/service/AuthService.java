package com.cofisweak.service;

import com.cofisweak.dao.UserRepository;
import com.cofisweak.exception.IncorrectPasswordException;
import com.cofisweak.exception.UserNotFoundException;
import com.cofisweak.exception.UsernameAlreadyExistsException;
import com.cofisweak.model.User;
import lombok.SneakyThrows;
import org.hibernate.exception.ConstraintViolationException;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

public class AuthService {
    private final UserRepository userRepository = new UserRepository();

    public User login(String username, String password) throws IncorrectPasswordException, UserNotFoundException {
        Optional<User> userOptional = userRepository.getUserByLogin(username);
        User user = userOptional.orElseThrow(UserNotFoundException::new);

        String hashedPassword = user.getPassword();
        if (!BCrypt.checkpw(password, hashedPassword)) {
            throw new IncorrectPasswordException();
        }
        return userOptional.get();
    }

    @SneakyThrows
    public User register(String username, String password) throws UsernameAlreadyExistsException {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = User.builder()
                .login(username)
                .password(hashedPassword)
                .build();
        try {
            userRepository.save(user);
        } catch (ConstraintViolationException e) {
            throw new UsernameAlreadyExistsException();
        }
        return user;
    }
}
