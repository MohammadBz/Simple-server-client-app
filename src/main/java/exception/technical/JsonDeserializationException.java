package exception.technical;

import exception.base.SystemException;

public class JsonDeserializationException extends SystemException {
    public JsonDeserializationException(String message, Exception cause) {
        super("Failed to parse incoming JSON: " + message, cause);
    }
}
