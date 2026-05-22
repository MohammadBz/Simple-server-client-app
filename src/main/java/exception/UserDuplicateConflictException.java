package exception;

public class UserDuplicateConflictException extends RuntimeException {
    public UserDuplicateConflictException(String message) {
        super(message);
    }
}
