package exception.business;

import exception.base.BusinessException;

public class AuthenticationException extends BusinessException {
    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Exception e) {
        super(message);
    }
}
