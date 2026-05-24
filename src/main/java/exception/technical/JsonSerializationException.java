package exception.technical;

import exception.base.SystemException;

public class JsonSerializationException extends SystemException {
    public JsonSerializationException(String message, Exception cause) {
        super("Failed to serialize outgoing response: " + message, cause);
    }
}
