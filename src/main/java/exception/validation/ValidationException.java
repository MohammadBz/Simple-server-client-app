package exception.validation;

import exception.base.BusinessException;

public class ValidationException extends BusinessException {
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Exception e) {
        super(message);
    }
}
