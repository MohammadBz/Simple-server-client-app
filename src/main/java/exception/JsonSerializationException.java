package exception;

public class JsonSerializationException extends SystemException {
    public JsonSerializationException(String message, Throwable cause) {
        super("Failed to serialize outgoing response: " + message);
    }
}
