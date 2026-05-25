package service.server.session;

import service.server.core.ClientConnection;

public interface ConnectionRegistry {
    void register(ClientConnection connection);

    void unregister(ClientConnection connection);

    void disconnectAll();

    int size();
}