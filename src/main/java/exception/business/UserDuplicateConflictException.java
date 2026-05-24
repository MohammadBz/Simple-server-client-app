package exception.business;

import exception.base.BusinessException;

public class UserDuplicateConflictException extends BusinessException {
    private final String username;

    public UserDuplicateConflictException(String message, String username) {
        super(message);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
