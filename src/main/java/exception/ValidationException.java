package exception;

public class ValidationException extends AppException {
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Exception e) {
        super(message);
    }
}
