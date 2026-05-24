package exception.validation;

public class LoginValidationException extends ValidationException {
    private final String username;

    public LoginValidationException(String message, String username) {
        super(message);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
