package validation;

import exception.MessageValidationException;
import exception.ValidationException;

public final class MessageValidator {

    private static final int MAX_MESSAGE_LENGTH = 1000;

    private MessageValidator() {
    }

    public static void validateRecipient(String recipient) {
        if (recipient == null || recipient.trim().isEmpty()) {
            throw new MessageValidationException("Recipient cannot be empty.");
        }
    }

    public static void validateContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new MessageValidationException("Message content cannot be empty.");
        }

        if (content.length() > MAX_MESSAGE_LENGTH) {
            throw new MessageValidationException("Message exceeds maximum allowed length.");
        }
    }

    public static void validateMessage(String recipient, String content) {
        validateRecipient(recipient);
        validateContent(content);
    }
}