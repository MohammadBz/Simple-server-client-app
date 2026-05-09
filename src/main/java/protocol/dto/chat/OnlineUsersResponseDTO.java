package protocol.dto.chat;

import java.util.List;

public class OnlineUsersResponseDTO {

    private List<String> users;

    public OnlineUsersResponseDTO() {
    }

    public OnlineUsersResponseDTO(List<String> users) {
        this.users = users;
    }

    public List<String> getUsers() {
        return users;
    }
}