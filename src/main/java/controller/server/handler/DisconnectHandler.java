package controller.server.handler;

import lombok.extern.slf4j.Slf4j;
import protocol.message.Message;
import protocol.response.factory.ResponseFactory;
import protocol.request.DisconnectRequest;
import service.server.core.base.ClientConnection;

@Slf4j
public class DisconnectHandler implements RequestHandler<DisconnectRequest> {
    private final ResponseFactory responseFactory;

    public DisconnectHandler(ResponseFactory responseFactory) {
        this.responseFactory = responseFactory;
    }

    @Override
    public Class<DisconnectRequest> requestType() {
        return DisconnectRequest.class;
    }

    @Override
    public void handle(DisconnectRequest request, ClientConnection clientConnection) {
        log.info("Client with id {} is disconnecting...", clientConnection.getClientId());

        Message response = responseFactory.disconnect();

        try {
            clientConnection.send(response);
        } catch (Exception ignored) {
            log.warn("Failed to send disconnect response");
        }
        clientConnection.stop();
    }
}
