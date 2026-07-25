package service.server.core.base;

public interface SessionOperations {
    void registerSession(String username, ClientConnection connection);

    void unregisterSession(String username);

    boolean isOnline(String username);
}
