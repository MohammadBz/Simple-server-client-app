package protocol.response.factory;

import domain.chat.MessageStatus;
import infrastructure.marshalling.Marshaller;
import protocol.message.Message;

import java.util.List;
import java.util.UUID;

public interface ResponseFactory {
    Message loginSuccess();

    Message loginFailure(String reason);

    Message signupSuccess();

    Message signupFailure(String reason);

    Message deliveryStatus(UUID messageId, MessageStatus status, String details);

    Message onlineUsers(List<String> users);

    Message disconnect();

    Message logout();

    Message systemNotification(String text);

}
