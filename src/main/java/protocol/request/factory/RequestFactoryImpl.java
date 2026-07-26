package protocol.request.factory;

import protocol.request.BaseRequest;
import protocol.request.DisconnectRequest;
import protocol.request.LoginRequest;
import protocol.request.LogoutRequest;
import protocol.request.OnlineUsersRequest;
import protocol.request.SendMessageRequest;
import protocol.request.SignupRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum RequestFactoryImpl implements RequestFactory {
    INSTANCE;

    private RequestFactoryImpl() {
    }

    public BaseRequest login(String username, String password) {
        log.debug("Creating LOGIN_REQUEST message for user: {}", username);
        return new LoginRequest(username, password);
    }

    public BaseRequest signup(String username, String password) {
        log.debug("Creating SIGNUP_REQUEST message for user: {}", username);
        return new SignupRequest(username, password);
    }

    public BaseRequest sendMessage(String sender, String receiver, String content) {
        log.debug("Creating SEND_MESSAGE_REQUEST from [{}] to [{}]", sender, receiver);
        return new SendMessageRequest(sender, receiver, content);

    }

    public BaseRequest onlineUsersRequest(String requester) {
        log.debug("Creating ONLINE_USERS_REQUEST for requester: {}", requester);
        return new OnlineUsersRequest(requester);
    }

    public BaseRequest disconnectRequest() {
        log.debug("Creating DISCONNECT_REQUEST");
        return new DisconnectRequest("client");
    }

    public BaseRequest logoutRequest(String username) {
        log.debug("Creating LOGOUT_REQUEST for user: {}", username);
        return new LogoutRequest(username);
    }
}
