package protocol.dto.chat;

import java.time.LocalDateTime;

public class IncomingMessageDTO {

    private String sender;
    private String content;
    private LocalDateTime timestamp;

    public IncomingMessageDTO() {
    }

    public IncomingMessageDTO(String sender, String content, LocalDateTime timestamp) {
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}