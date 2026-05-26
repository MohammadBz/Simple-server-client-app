package service.client;

import exception.technical.ConnectionException;
import infrastructure.network.ConnectionManager;
import infrastructure.serialization.Serializer;
import lombok.extern.slf4j.Slf4j;
import protocol.message.Message;
import launcher.client.event.ClientEventHandler;
import service.client.ResponseDispatcher;

@Slf4j
public class ClientListener implements Runnable {

    private final ConnectionManager connectionManager;
    private final ClientEventHandler eventHandler;
    private final Serializer serializer;
    private final ResponseDispatcher responseDispatcher;

    private volatile boolean running = true;

    public ClientListener(ConnectionManager connectionManager, ClientEventHandler clientEventHandler, Serializer serializer, ResponseDispatcher responseDispatcher) {
        this.connectionManager = connectionManager;
        this.eventHandler = clientEventHandler;
        this.serializer = serializer;
        this.responseDispatcher = responseDispatcher;
    }

    @Override
    public void run() {
        try {
            while (running) {
                Message message = serializer.deserialize(connectionManager.receive(), Message.class);
                log.debug("Received message: {}", message.getType());

                responseDispatcher.dispatch(message);
            }
        } catch (ConnectionException e) {
            if ("CONNECTION_CLOSED".equals(e.getMessage())) {
                log.info("Listener stopped: normal disconnect");
                eventHandler.onConnectionLost("Disconnected");
            } else {
                log.error("Listener stopped due to network error", e);
                eventHandler.onConnectionLost("Network error");
            }
        } catch (Exception e) {
            log.error("Listener stopped due to error : {}", e.getMessage());
            eventHandler.onConnectionLost("Network error");
        }

    }

    public void stop() {
        running = false;
    }
}