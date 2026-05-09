package exception;

public class AuthenticationException extends AppException {
    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Exception e) {
        super(message);
    }
}
