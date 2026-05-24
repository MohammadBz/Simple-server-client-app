package exception.business;

public class InvalidCredentialsException extends AuthenticationException {
    private final String username;

    public InvalidCredentialsException(String message, String username) {
        super(message);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
