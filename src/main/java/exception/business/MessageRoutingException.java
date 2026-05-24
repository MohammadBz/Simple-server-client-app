package exception.business;

import exception.base.BusinessException;

public class MessageRoutingException extends BusinessException {
    public MessageRoutingException(String message) {
        super(message);
    }

    public MessageRoutingException(String message, Exception e) {
        super(message);
    }
}
