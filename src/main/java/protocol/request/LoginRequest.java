package protocol.request;

public class LoginRequest extends BaseRequest {

    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String sender, String password) {
        super(sender);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
