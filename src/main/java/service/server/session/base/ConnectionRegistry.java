package service.server.session.base;

import service.server.core.base.ClientConnection;

public interface ConnectionRegistry {
    void register(ClientConnection connection);

    void unregister(ClientConnection connection);

    void disconnectAll();

    int size();
}