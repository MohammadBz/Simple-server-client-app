package protocol.dto.chat;

import domain.chat.MessageStatus;

import java.util.UUID;

public class DeliveryStatusDTO {

    private UUID messageId;
    private MessageStatus status;
    private String details;

    public DeliveryStatusDTO() {
    }

    public DeliveryStatusDTO(UUID messageId, MessageStatus status, String details) {
        this.messageId = messageId;
        this.status = status;
        this.details = details;
    }

    public UUID getMessageId() {
        return messageId;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public String getDetails() {
        return details;
    }
}