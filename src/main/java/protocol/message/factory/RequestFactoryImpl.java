package protocol.message.factory;

import infrastructure.serialization.Serializer;
import protocol.message.Message;
import protocol.message.MessageType;
import protocol.dto.auth.LoginRequestDTO;
import protocol.dto.auth.SignupRequestDTO;
import protocol.dto.chat.SendMessageRequestDTO;
import protocol.dto.chat.OnlineUsersRequestDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class RequestFactoryImpl implements RequestFactory {
    private final Serializer serializer;

    public RequestFactoryImpl(Serializer serializer) {
        this.serializer = serializer;
    }

    public Message login(String username, String password) {
        log.debug("Creating LOGIN_REQUEST message for user: {}", username);
        LoginRequestDTO dto = new LoginRequestDTO(username, password);
        return new Message(MessageType.LOGIN_REQUEST, username, serializer.serialize(dto));
    }

    public Message signup(String username, String password) {
        log.debug("Creating SIGNUP_REQUEST message for user: {}", username);
        SignupRequestDTO dto = new SignupRequestDTO(username, password);
        return new Message(MessageType.SIGNUP_REQUEST, username, serializer.serialize(dto));
    }

    public Message sendMessage(String sender, String receiver, String content) {
        log.debug("Creating SEND_MESSAGE_REQUEST from [{}] to [{}]", sender, receiver);
        SendMessageRequestDTO dto = new SendMessageRequestDTO(receiver, content);
        return new Message(MessageType.SEND_MESSAGE_REQUEST, sender, serializer.serialize(dto));

    }


    public Message onlineUsersRequest(String requester) {
        log.debug("Creating ONLINE_USERS_REQUEST for requester: {}", requester);
        OnlineUsersRequestDTO dto = new OnlineUsersRequestDTO(requester);
        return new Message(MessageType.ONLINE_USERS_REQUEST, requester, serializer.serialize(dto));
    }

    public Message disconnectRequest() {
        log.debug("Creating DISCONNECT_REQUEST");
        return new Message(MessageType.DISCONNECT_REQUEST, "client", "{}");
    }

    public Message logoutRequest(String username) {
        log.debug("Creating LOGOUT_REQUEST for user: {}", username);
        return new Message(MessageType.LOGOUT_REQUEST, username, "{}");
    }
}