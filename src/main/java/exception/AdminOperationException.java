package exception;

public class AdminOperationException extends AppException {
    public AdminOperationException(String message) {
        super(message);
    }

    public AdminOperationException(String message, Exception cause) {
        super(message, cause);
    }
}
