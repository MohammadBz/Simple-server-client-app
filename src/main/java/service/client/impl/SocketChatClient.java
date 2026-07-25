package service.client.impl;

import infrastructure.serialization.Serializer;
import launcher.client.event.ClientEventHandler;
import exception.technical.ConnectionException;
import lombok.extern.slf4j.Slf4j;
import protocol.request.BaseRequest;

@Slf4j
public class SocketChatClient extends AbstractClient {
    public SocketChatClient(ClientEventHandler eventHandler, Serializer serializer) {
        super(eventHandler, serializer);
    }

    @Override
    public void send(BaseRequest message) throws ConnectionException {
        connectionManager.send(serializer.serialize(message));
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
