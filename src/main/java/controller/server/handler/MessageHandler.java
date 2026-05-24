package controller.server.handler;

import protocol.message.Message;
import exception.business.MessageProcessingException;
import service.server.core.ClientHandler;

public interface MessageHandler {
    public void handle(Message message, ClientHandler clientHandler) throws MessageProcessingException;
}
