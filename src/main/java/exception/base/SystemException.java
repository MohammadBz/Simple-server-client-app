package exception.base;

public class SystemException extends AppException {
    public SystemException(String message) {
        super(message);
    }

    public SystemException(String message, Exception e) {
        super(message, e);
    }
}
