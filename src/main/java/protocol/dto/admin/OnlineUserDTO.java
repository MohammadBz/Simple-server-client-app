package protocol.dto.admin;

public class OnlineUserDTO {


    private String username;
    private String status;

    public OnlineUserDTO(String username, String status) {
        this.username = username;
        this.status = status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }
}
