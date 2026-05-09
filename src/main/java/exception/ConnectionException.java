package exception;

public class ConnectionException extends AppException {
    public ConnectionException(String message) {
        super(message);
    }

    public ConnectionException(String message, Exception e) {
        super(message);
    }
}
