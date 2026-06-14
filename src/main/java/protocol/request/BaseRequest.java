package protocol.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = LoginRequest.class, name = "LOGIN_REQUEST"),
        @JsonSubTypes.Type(value = SignupRequest.class, name = "SIGNUP_REQUEST"),
        @JsonSubTypes.Type(value = SendMessageRequest.class, name = "SEND_MESSAGE_REQUEST"),
        @JsonSubTypes.Type(value = OnlineUsersRequest.class, name = "ONLINE_USERS_REQUEST"),
        @JsonSubTypes.Type(value = DisconnectRequest.class, name = "DISCONNECT_REQUEST"),
        @JsonSubTypes.Type(value = LogoutRequest.class, name = "LOGOUT_REQUEST")
})
public abstract class BaseRequest {

    private String sender;

    protected BaseRequest() {
    }

    protected BaseRequest(String sender) {
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
