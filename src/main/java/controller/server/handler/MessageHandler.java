package controller.server.handler;

import protocol.message.Message;
import exception.business.MessageProcessingException;
import service.server.core.ClientConnection;

public interface MessageHandler {
    public void handle(Message message, ClientConnection Clientconnection) throws MessageProcessingException;
}
