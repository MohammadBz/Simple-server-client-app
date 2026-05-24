package exception.business;

import exception.base.BusinessException;

public class MessageProcessingException extends BusinessException {
    public MessageProcessingException(String message, Exception e) {
        super(message);
    }

    public MessageProcessingException(String message) {
        super(message);
    }
}
