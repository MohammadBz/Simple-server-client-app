package service.client;

import ch.qos.logback.core.net.server.Client;
import launcher.client.event.ClientEventHandler;
import protocol.message.Message;
import exception.technical.ConnectionException;
import lombok.extern.slf4j.Slf4j;
import infrastructure.network.ConnectionManager;

@Slf4j
public class SocketChatClient extends AbstractClient {
    public SocketChatClient(ClientEventHandler eventHandler) {
        super(eventHandler);
    }

    @Override
    public void send(Message message) throws ConnectionException {
        connectionManager.send(message.toJson());
    }

    @Override
    public void disconnect() {
        if (listener != null) {
            listener.stop();
        }
        connectionManager.close();
        log.info("Client disconnected");
    }


}