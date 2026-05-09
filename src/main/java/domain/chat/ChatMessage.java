package domain.chat;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class ChatMessage {

    private final UUID id;
    private final String sender;
    private final String recipient;
    private final String content;
    private final LocalDateTime timestamp;
    private MessageStatus status;

    public ChatMessage(String sender, String recipient, String content) {
        this.id = UUID.randomUUID();
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
        this.timestamp = LocalDateTime.now();
        this.status = MessageStatus.CREATED;
    }

    public UUID getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void updateStatus(MessageStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatMessage that)) return false;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}