package protocol.response.factory;

import infrastructure.marshalling.Marshaller;
import infrastructure.marshalling.MarshallerStrategy;
import protocol.response.ResponseDTO;
import protocol.response.ResponseMessages;
import protocol.message.Message;
import protocol.message.MessageType;
import domain.chat.MessageStatus;
import protocol.dto.chat.DeliveryStatusDTO;
import protocol.dto.chat.OnlineUsersResponseDTO;

import java.util.List;
import java.util.UUID;

public enum ResponseFactoryImpl implements ResponseFactory {
    INSTANCE;
    private final Marshaller<String> marshaller = MarshallerStrategy.getMarshaller();

    private ResponseFactoryImpl() {
    }


    @Override
    public Message loginSuccess() {
        return buildResponse(MessageType.LOGIN_RESPONSE, true, ResponseMessages.LOGIN_SUCCESS);
    }

    @Override
    public Message loginFailure(String reason) {
        return buildResponse(MessageType.LOGIN_RESPONSE, false, reason);
    }

    @Override
    public Message signupSuccess() {
        return buildResponse(MessageType.SIGNUP_RESPONSE, true, ResponseMessages.SIGNUP_SUCCESS);
    }

    @Override
    public Message signupFailure(String reason) {
        return buildResponse(MessageType.SIGNUP_RESPONSE, false, reason);
    }

    @Override
    public Message deliveryStatus(UUID messageId, MessageStatus status, String details) {

        DeliveryStatusDTO dto = new DeliveryStatusDTO(messageId, status, details);

        return new Message(MessageType.DELIVERY_STATUS, "launcher", marshaller.marshall(dto));
    }

    @Override
    public Message onlineUsers(List<String> users) {

        OnlineUsersResponseDTO dto = new OnlineUsersResponseDTO(users);

        return new Message(MessageType.ONLINE_USERS_RESPONSE, "launcher", marshaller.marshall(dto));
    }

    @Override
    public Message disconnect() {
        return buildResponse(MessageType.DISCONNECT_RESPONSE, true, "Disconnected successfully");
    }

    @Override
    public Message logout() {
        return buildResponse(MessageType.LOGOUT_RESPONSE, true, "Logout successfully");
    }

    @Override
    public Message systemNotification(String text) {
        return buildResponse(MessageType.SYSTEM_NOTIFICATION, true, text);
    }

    private Message buildResponse(MessageType type, boolean success, String text) {
        ResponseDTO dto = new ResponseDTO(success, text);
        return new Message(type, "launcher", marshaller.marshall(dto));
    }
}