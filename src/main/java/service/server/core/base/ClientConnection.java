package service.server.core.base;

import domain.chat.ChatMessage;
import protocol.message.Message;
import service.server.session.impl.Session;

public interface ClientConnection {
    public void send(Message message);

    public void disconnect(String reason);

    public void stop();

    public Session getSession();

    public String getClientId();

    public void deliver(ChatMessage chatMessage);
}
