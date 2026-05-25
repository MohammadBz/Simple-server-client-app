package protocol.message.factory;

import infrastructure.serialization.Serializer;
import protocol.response.ResponseDTO;
import protocol.response.ResponseMessages;
import protocol.message.Message;
import protocol.message.MessageType;
import domain.chat.MessageStatus;
import protocol.dto.chat.DeliveryStatusDTO;
import protocol.dto.chat.OnlineUsersResponseDTO;
import infrastructure.serialization.JacksonSerializer;

import java.util.List;
import java.util.UUID;

public final class ResponseFactory {

    private static Serializer serializer;

    static {
        serializer = new JacksonSerializer();
    }

    private ResponseFactory() {
    }

    public static Message loginSuccess() {
        return build(MessageType.LOGIN_RESPONSE, true, ResponseMessages.LOGIN_SUCCESS);
    }

    public static Message loginFailure(String reason) {
        return build(MessageType.LOGIN_RESPONSE, false, reason);
    }

    public static Message signupSuccess() {
        return build(MessageType.SIGNUP_RESPONSE, true, ResponseMessages.SIGNUP_SUCCESS);
    }

    public static Message signupFailure(String reason) {
        return build(MessageType.SIGNUP_RESPONSE, false, reason);
    }

    public static Message deliveryStatus(UUID messageId, MessageStatus status, String details) {

        DeliveryStatusDTO dto = new DeliveryStatusDTO(messageId, status, details);

        return new Message(MessageType.DELIVERY_STATUS, "launcher", serializer.serialize(dto));
    }

    public static Message onlineUsers(List<String> users) {

        OnlineUsersResponseDTO dto = new OnlineUsersResponseDTO(users);

        return new Message(MessageType.ONLINE_USERS_RESPONSE, "launcher", serializer.serialize(dto));
    }

    public static Message disconnect() {
        return build(MessageType.DISCONNECT_RESPONSE, true, "Disconnected successfully");
    }

    public static Message logout() {
        return build(MessageType.LOGOUT_RESPONSE, true, "Logout successfully");
    }

    public static Message systemNotification(String text) {

        return build(MessageType.SYSTEM_NOTIFICATION, true, text);
    }

    private static Message build(MessageType type, boolean success, String text) {
        ResponseDTO dto = new ResponseDTO(success, text);
        return new Message(type, "launcher", serializer.serialize(dto));
    }
}