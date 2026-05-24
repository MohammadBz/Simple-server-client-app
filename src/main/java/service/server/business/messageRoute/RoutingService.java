package service.server.business.messageRoute;

import exception.business.MessageRoutingException;
import lombok.extern.slf4j.Slf4j;
import domain.chat.ChatMessage;
import domain.chat.MessageStatus;
import service.server.core.ClientConnection;
import service.server.session.SessionRegistry;

@Slf4j
public class RoutingService {

    private final SessionRegistry sessionRegistry;

    public RoutingService(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    public ChatMessage route(ChatMessage chatMessage) throws MessageRoutingException {

        ClientConnection recipientHandler = sessionRegistry.getClient(chatMessage.getRecipient());

        if (recipientHandler == null) {
            chatMessage.updateStatus(MessageStatus.FAILED);
            log.warn("Routing failed - recipient '{}' offline", chatMessage.getRecipient());
            throw new MessageRoutingException("Recipient is offline or unavailable");
        }

        recipientHandler.deliver(chatMessage);

        chatMessage.updateStatus(MessageStatus.DELIVERED);

        log.info("Message {} delivered from '{}' to '{}'", chatMessage.getId(), chatMessage.getSender(), chatMessage.getRecipient());

        return chatMessage;
    }
}