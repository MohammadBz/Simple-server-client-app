package service.client;

import protocol.message.Message;

public interface ChatClient {
    void connect(String host, int port);

    void send(Message message);

    void disconnect();
}
