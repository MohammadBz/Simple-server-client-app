package protocol.message;


public class Message {

    private MessageType type;
    private String sender;
    private String payload;

    public Message(MessageType type, String sender, String payload) {
        this.type = type;
        this.sender = sender;
        this.payload = payload;
    }

    public Message() {
    }


    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}