package exception;

public class MessageProcessingException extends AppException {
    public MessageProcessingException(String message,Exception e) {
        super(message);
    }
    public MessageProcessingException(String message) {
        super(message);
    }
}
