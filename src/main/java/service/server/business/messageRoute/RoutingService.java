package service.server.business.messageRoute;

import domain.chat.ChatMessage;

public interface RoutingService {
    public ChatMessage route(ChatMessage chatMessage);
}
