package controller.server.handler;

import protocol.message.factory.ResponseFactory;
import service.server.core.ClientConnection;
import lombok.extern.slf4j.Slf4j;
import protocol.message.Message;


@Slf4j
public class DisconnectHandler implements MessageHandler {
    private final ResponseFactory responseFactory;

    public DisconnectHandler(ResponseFactory responseFactory) {
        this.responseFactory = responseFactory;
    }

    @Override
    public void handle(Message message, ClientConnection clientConnection) {

        log.info("Client with id  {}  is disconnecting...", clientConnection.getClientId());

        Message response = responseFactory.disconnect();

        try {
            clientConnection.send(response);
        } catch (Exception ignored) {
            log.warn("Failed to send disconnect response");
        }
        clientConnection.stop();
    }
}