package exception;

public class AppException extends Exception {
    public AppException(String message) {
        super(message);
    }

    public AppException(String message, Exception e) {
        super(message);
    }

}
