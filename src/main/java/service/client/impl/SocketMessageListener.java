package service.client.impl;

import exception.technical.ConnectionException;
import infrastructure.network.ConnectionManager;
import infrastructure.serialization.Serializer;
import lombok.extern.slf4j.Slf4j;
import protocol.message.Message;
import launcher.client.event.ClientEventHandler;

@Slf4j
public class SocketMessageListener extends AbstractMessageListener {
    private final ConnectionManager connectionManager;
    private final Serializer serializer;
    private final ResponseDispatcher responseDispatcher;
    private final ClientEventHandler eventHandler;

    public SocketMessageListener(ConnectionManager connectionManager, Serializer serializer, ResponseDispatcher responseDispatcher, ClientEventHandler eventHandler) {
        this.connectionManager = connectionManager;
        this.serializer = serializer;
        this.responseDispatcher = responseDispatcher;
        this.eventHandler = eventHandler;
    }

    @Override
    protected void listenAndProcess() {
        String rawData = connectionManager.receive();
        Message message = serializer.deserialize(rawData, Message.class);
        log.debug("Received message: {}", message.getType());
        responseDispatcher.dispatch(message);
    }

    @Override
    protected void handleError(Exception e) {
        if (e instanceof ConnectionException && "CONNECTION_CLOSED".equals(e.getMessage())) {
            log.info("Normal disconnect detected.");
            eventHandler.onConnectionLost("Disconnected");
        } else {
            log.error("Listener error: {}", e.getMessage());
            eventHandler.onConnectionLost("Network error");
        }
    }
}