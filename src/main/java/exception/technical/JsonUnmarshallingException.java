package exception.technical;

import exception.base.SystemException;

public class JsonUnmarshallingException extends SystemException {
    public JsonUnmarshallingException(String message, Exception cause) {
        super("Failed to parse incoming JSON: " + message, cause);
    }
}
