package service.server.core;

import domain.chat.ChatMessage;

public interface MessageOperations {
    ChatMessage sendMessage(ChatMessage chatMessage);
}
