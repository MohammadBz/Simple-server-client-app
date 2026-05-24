package service.server.core;

import domain.chat.ChatMessage;
import protocol.message.Message;
import service.server.session.Session;

public interface ClientConnection {
    public void send(Message message);

    public void disconnect(String reason);

    public Session getSession();

    public String getClientId();

    public void deliver(ChatMessage chatMessage);
}
