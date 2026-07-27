package exception.technical;

import exception.base.SystemException;

public class JsonMarshallingException extends SystemException {
    public JsonMarshallingException(String message, Exception cause) {
        super("Failed to marshall outgoing response: " + message, cause);
    }
}
