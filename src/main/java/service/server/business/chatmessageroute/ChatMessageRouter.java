package service.server.business.chatmessageroute;

import domain.chat.ChatMessage;

public interface ChatMessageRouter {
    ChatMessage route(ChatMessage chatMessage);
}
