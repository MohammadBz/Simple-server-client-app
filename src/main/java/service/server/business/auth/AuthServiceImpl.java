package service.server.business.auth;

import domain.user.User;
import exception.business.InvalidCredentialsException;
import exception.business.UserDuplicateConflictException;

import java.util.concurrent.ConcurrentHashMap;

public class AuthServiceImpl implements AuthService {

    private final ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

    @Override
    public void signup(String username, String password) {
        User newUser = new User(username, password);

        if (userExists(username)) {
            throw new UserDuplicateConflictException("User already exists", username);
        }
        users.put(username, newUser);
    }

    @Override
    public void login(String username, String password) {
        User user = users.get(username);
        if (!userExists(username)) {
            throw new InvalidCredentialsException("User not found", username);
        }
        if (userExists(username) && !user.getPassword().equals(password)) {
            throw new InvalidCredentialsException("Invalid credentials", username);
        }
    }

    @Override
    public boolean userExists(String username) {
        return users.containsKey(username);
    }
}