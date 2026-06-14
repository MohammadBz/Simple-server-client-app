package service.client;

import protocol.request.BaseRequest;

public interface ChatClient {
    void connect(String host, int port);

    void send(BaseRequest message);

    void disconnect();
}
