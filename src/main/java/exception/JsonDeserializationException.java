package exception;

public class JsonDeserializationException extends SystemException {
    public JsonDeserializationException(String message, Throwable cause) {
        super("Failed to parse incoming JSON: " + message);
    }
}
