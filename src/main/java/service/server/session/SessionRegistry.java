package service.server.session;

import service.server.core.ClientConnection;

import java.util.List;

public interface SessionRegistry {
    void register(String username, ClientConnection connection);

    void unregister(String username);

    ClientConnection getClient(String username);

    boolean exists(String username);

    List<String> getOnlineUsers();

    boolean disconnect(String username);

    void disconnectAll();
}
