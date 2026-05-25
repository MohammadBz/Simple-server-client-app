package protocol.message.factory;

import domain.chat.MessageStatus;
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
