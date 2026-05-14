package handler.server;

import service.core.ClientHandler;
import service.core.ServerManager;
import lombok.extern.slf4j.Slf4j;
import protocol.message.Message;
import protocol.message.factory.ResponseFactory;

@Slf4j
public class DisconnectHandler implements MessageHandler {

    private final ServerManager serverManager;

    public DisconnectHandler(ServerManager serverManager) {
        this.serverManager = serverManager;
    }

    @Override
    public void handle(Message message, ClientHandler clientHandler) {

        log.info("Client with id  {}  is disconnecting...", clientHandler.getClientId());

        Message response = ResponseFactory.disconnect();

        try {
            clientHandler.send(response);
        } catch (Exception ignored) {
            log.warn("Failed to send disconnect response");
        }
        clientHandler.close();
    }
}