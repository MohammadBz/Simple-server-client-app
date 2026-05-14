package service.client;

import app.client.event.ClientEventHandler;
import protocol.message.Message;
import exception.ConnectionException;
import lombok.extern.slf4j.Slf4j;
import infrastructure.network.ConnectionManager;

@Slf4j
public class Client {

    private final ConnectionManager connectionManager;
    private ClientListener listener;
    private ClientEventHandler eventHandler;

    public Client(ClientEventHandler eventHandler) {
        this.connectionManager = new ConnectionManager();
        this.eventHandler = eventHandler;
    }

    public void connect(String host, int port) throws ConnectionException {
        try {
            connectionManager.connect(host, port);

            listener = new ClientListener(connectionManager, eventHandler);
            new Thread(listener).start();

        } catch (ConnectionException e) {
            log.error("Failed to connect to server", e);
            throw e;
        }
    }

    public void send(Message message) throws ConnectionException {
        try {
            connectionManager.send(message.toJson());
        } catch (ConnectionException e) {
            log.error("Failed to send message", e);
            throw e;
        }
    }

    public void disconnect() {
        if (listener != null) {
            listener.stop();
        }

        try {
            connectionManager.close();
        } catch (Exception e) {
            log.warn("Error while closing connection", e);
        }

        log.info("Client disconnected");
    }

    public void setEventHandler(ClientEventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }
}