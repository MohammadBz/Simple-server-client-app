package service.server.business.chatmessageroute;

import domain.chat.ChatMessage;
import domain.chat.MessageStatus;
import exception.business.MessageRoutingException;
import lombok.extern.slf4j.Slf4j;
import service.server.core.base.ClientConnection;
import service.server.session.base.SessionRegistry;

@Slf4j
public class ChatMessageRouterImpl implements ChatMessageRouter {

    private final SessionRegistry sessionRegistry;

    public ChatMessageRouterImpl(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    @Override
    public ChatMessage route(ChatMessage chatMessage) {

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
