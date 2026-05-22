package exception;

public class SignupValidationException extends ValidationException {
    private final String username;

    public SignupValidationException(String message, String username) {
        super(message);
        this.username = username;
    }
    public String getUsername() {
        return username;
    }
}
