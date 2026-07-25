package service.client.impl;

import infrastructure.marshalling.Marshaller;
import launcher.client.event.ClientEventHandler;
import exception.technical.ConnectionException;
import lombok.extern.slf4j.Slf4j;
import protocol.request.BaseRequest;

@Slf4j
public class SocketChatClient extends AbstractClient {
    public SocketChatClient(ClientEventHandler eventHandler, Marshaller<String> marshaller) {
        super(eventHandler, marshaller);
    }

    @Override
    public void send(BaseRequest message) throws ConnectionException {
        connectionManager.send(marshaller.marshall(message));
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
