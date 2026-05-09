package protocol.message.factory;

import protocol.message.Message;
import protocol.message.MessageType;
import protocol.dto.auth.LoginRequestDTO;
import protocol.dto.auth.SignupRequestDTO;
import protocol.dto.chat.SendMessageRequestDTO;
import protocol.dto.chat.OnlineUsersRequestDTO;
import lombok.extern.slf4j.Slf4j;
import infrastructure.serialization.JsonUtil;

@Slf4j
public final class RequestFactory {

    private RequestFactory() {
    }

    public static Message login(String username, String password) {
        log.debug("Creating LOGIN_REQUEST message for user: {}", username);
        LoginRequestDTO dto = new LoginRequestDTO(username, password);
        return new Message(MessageType.LOGIN_REQUEST, username, JsonUtil.toJson(dto));
    }

    public static Message signup(String username, String password) {
        log.debug("Creating SIGNUP_REQUEST message for user: {}", username);
        SignupRequestDTO dto = new SignupRequestDTO(username, password);
        return new Message(MessageType.SIGNUP_REQUEST, username, JsonUtil.toJson(dto));
    }

    public static Message sendMessage(String sender, String receiver, String content) {
        log.debug("Creating SEND_MESSAGE_REQUEST from [{}] to [{}]", sender, receiver);
        SendMessageRequestDTO dto = new SendMessageRequestDTO(receiver, content);
        return new Message(MessageType.SEND_MESSAGE_REQUEST, sender, JsonUtil.toJson(dto));

    }


    public static Message onlineUsersRequest(String requester) {
        log.debug("Creating ONLINE_USERS_REQUEST for requester: {}", requester);
        OnlineUsersRequestDTO dto = new OnlineUsersRequestDTO(requester);
        return new Message(MessageType.ONLINE_USERS_REQUEST, requester, JsonUtil.toJson(dto));
    }

    public static Message disconnectRequest() {
        log.debug("Creating DISCONNECT_REQUEST");
        return new Message(MessageType.DISCONNECT_REQUEST, "client", "{}");
    }

    public static Message logoutRequest(String username) {
        log.debug("Creating LOGOUT_REQUEST for user: {}", username);
        return new Message(MessageType.LOGOUT_REQUEST, username, "{}");
    }
}