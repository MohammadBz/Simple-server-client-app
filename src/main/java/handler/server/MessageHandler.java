package handler.server;

import protocol.message.Message;
import exception.MessageProcessingException;
import service.core.ClientHandler;

public interface MessageHandler {
    public void handle(Message message, ClientHandler clientHandler) throws MessageProcessingException;
}
