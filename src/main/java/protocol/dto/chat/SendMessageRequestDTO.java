package protocol.dto.chat;

public class SendMessageRequestDTO {

    private String recipient;
    private String content;

    public SendMessageRequestDTO() {
    }

    public SendMessageRequestDTO(String recipient, String content) {
        this.recipient = recipient;
        this.content = content;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getContent() {
        return content;
    }
}