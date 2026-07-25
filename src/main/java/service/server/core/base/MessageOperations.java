package service.server.core.base;

import domain.chat.ChatMessage;

public interface MessageOperations {
    ChatMessage sendMessage(ChatMessage chatMessage);
}
