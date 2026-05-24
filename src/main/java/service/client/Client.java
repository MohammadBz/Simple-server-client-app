package service.client;

import launcher.client.event.ClientEventHandler;
import protocol.message.Message;
import exception.technical.ConnectionException;
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
        connectionManager.connect(host, port);

        listener = new ClientListener(connectionManager, eventHandler);
        new Thread(listener).start();
    }

    public void send(Message message) throws ConnectionException {
        connectionManager.send(message.toJson());
    }

    public void disconnect() {
        if (listener != null) {
            listener.stop();
        }
        connectionManager.close();
        log.info("Client disconnected");
    }

    public void setEventHandler(ClientEventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }
}