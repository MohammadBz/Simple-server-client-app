package protocol.request;

public class SignupRequest extends BaseRequest {

    private String password;

    public SignupRequest() {
    }

    public SignupRequest(String sender, String password) {
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
