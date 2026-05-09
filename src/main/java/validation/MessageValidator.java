package validation;

import exception.ValidationException;

public final class MessageValidator {

    private static final int MAX_MESSAGE_LENGTH = 1000;

    private MessageValidator() {
    }

    public static void validateRecipient(String recipient) throws ValidationException {
        if (recipient == null || recipient.trim().isEmpty()) {
            throw new ValidationException("Recipient cannot be empty.");
        }
    }

    public static void validateContent(String content) throws ValidationException {
        if (content == null || content.trim().isEmpty()) {
            throw new ValidationException("Message content cannot be empty.");
        }

        if (content.length() > MAX_MESSAGE_LENGTH) {
            throw new ValidationException("Message exceeds maximum allowed length.");
        }
    }

    public static void validateMessage(String recipient, String content) throws ValidationException {
        validateRecipient(recipient);
        validateContent(content);
    }
}