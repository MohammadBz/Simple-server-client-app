package application.messaging;

import exception.MessageRoutingException;
import lombok.extern.slf4j.Slf4j;
import domain.chat.ChatMessage;
import domain.chat.MessageStatus;
import application.core.ClientHandler;
import application.session.SessionRegistry;

@Slf4j
public class RoutingService {

    private final SessionRegistry sessionRegistry;

    public RoutingService(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    public ChatMessage route(ChatMessage chatMessage) throws MessageRoutingException {

        ClientHandler recipientHandler = sessionRegistry.getClient(chatMessage.getRecipient());

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