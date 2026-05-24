package controller.server.handler;

import service.server.core.ClientConnection;
import service.server.core.ServerManager;
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
    public void handle(Message message, ClientConnection clientConnection) {

        log.info("Client with id  {}  is disconnecting...", clientConnection.getClientId());

        Message response = ResponseFactory.disconnect();

        try {
            clientConnection.send(response);
        } catch (Exception ignored) {
            log.warn("Failed to send disconnect response");
        }
        clientConnection.stop();
    }
}