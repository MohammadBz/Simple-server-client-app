package exception;

public class JsonSerializationException extends SystemException {
    public JsonSerializationException(String message, Exception cause) {
        super("Failed to serialize outgoing response: " + message, cause);
    }
}
