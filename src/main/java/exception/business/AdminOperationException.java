package exception.business;

import exception.base.BusinessException;

public class AdminOperationException extends BusinessException {
    public AdminOperationException(String message) {
        super(message);
    }

    public AdminOperationException(String message, Exception cause) {
        super(message, cause);
    }
}
