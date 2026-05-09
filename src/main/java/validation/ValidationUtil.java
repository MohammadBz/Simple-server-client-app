package validation;

import exception.ValidationException;

public class ValidationUtil {

    public static void validateUsername(String username) throws ValidationException {
        if (username == null || username.trim().isEmpty()) {
            throw new ValidationException("Username cannot be empty");
        }
    }

    public static void validatePassword(String password) throws ValidationException {
        if (password == null || password.trim().isEmpty()) {
            throw new ValidationException("Password cannot be empty");
        }
    }

    public static void validateCredentials(String username, String password) throws ValidationException {
        validateUsername(username);
        validatePassword(password);
    }
}