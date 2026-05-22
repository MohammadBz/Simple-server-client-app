package service.auth;

import domain.user.User;
import exception.AuthenticationException;
import exception.InvalidCredentialsException;
import exception.UserDuplicateConflictException;

import java.util.concurrent.ConcurrentHashMap;

public class AuthService {

    private final ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

    public void signup(String username, String password) {
        User newUser = new User(username, password);

        if (userExists(username)) {
            throw new UserDuplicateConflictException("User already exists");
        }
        users.put(username, newUser);
    }

    public void login(String username, String password) {
        User user = users.get(username);
        if (!userExists(username)) {
            throw new InvalidCredentialsException("User not found");
        }
        if (userExists(username) && !user.getPassword().equals(password)) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
    }

    public boolean userExists(String username) {
        return users.containsKey(username);
    }
}