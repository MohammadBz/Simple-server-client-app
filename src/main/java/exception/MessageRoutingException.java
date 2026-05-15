package exception;

public class MessageRoutingException extends BusinessException {
    public MessageRoutingException(String message) {
        super(message);
    }

    public MessageRoutingException(String message, Exception e) {
        super(message);
    }
}
