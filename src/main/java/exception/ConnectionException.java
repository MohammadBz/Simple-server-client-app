package exception;

public class ConnectionException extends SystemException {
    public ConnectionException(String message) {
        super(message);
    }

    public ConnectionException(String message, Exception e) {
        super(message);
    }
}
