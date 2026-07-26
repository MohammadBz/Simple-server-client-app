package service.server.business.auth;

import domain.user.User;
import exception.business.InvalidCredentialsException;
import exception.business.UserDuplicateConflictException;

import java.util.concurrent.ConcurrentHashMap;

public enum AuthServiceImpl implements AuthService {
    INSTANCE;
    private final ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

    private AuthServiceImpl() {
    }

    @Override

    public void signup(String username, String password) {
        User newUser = new User(username, password);

        if (users.putIfAbsent(username, newUser) != null) {
            throw new UserDuplicateConflictException("User already exists", username);
        }
    }

    @Override
    public void login(String username, String password) {
        User user = users.get(username);
        if (!userExists(username)) {
            throw new InvalidCredentialsException("User not found", username);
        }
        if (!user.getPassword().equals(password)) {
            throw new InvalidCredentialsException("Invalid credentials", username);
        }
    }

    @Override
    public boolean userExists(String username) {
        return users.containsKey(username);
    }
}