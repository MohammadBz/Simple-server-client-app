package protocol.dto.chat;

public class OnlineUsersRequestDTO {

    private String requester;

    public OnlineUsersRequestDTO() {
    }

    public OnlineUsersRequestDTO(String requester) {
        this.requester = requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public String getRequester() {
        return requester;
    }
}
