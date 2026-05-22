package exception;

public class UserDuplicateConflictException extends RuntimeException {
    private final String username;

    public UserDuplicateConflictException(String message, String username) {
        super(message);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
