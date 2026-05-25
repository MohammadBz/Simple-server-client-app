package service.common.validation;

import exception.validation.ValidationException;

public final class UserValidator {
    private UserValidator() {
    }

    public static void validateUsername(String username) throws ValidationException {
        if (username == null || username.isBlank()) {
            throw new ValidationException("Username cannot be empty");
        }
    }
}