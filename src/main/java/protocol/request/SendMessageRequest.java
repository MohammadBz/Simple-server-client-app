package protocol.request;

public class SendMessageRequest extends BaseRequest {

    private String recipient;
    private String content;

    public SendMessageRequest() {
    }

    public SendMessageRequest(String sender, String recipient, String content) {
        super(sender);
        this.recipient = recipient;
        this.content = content;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
