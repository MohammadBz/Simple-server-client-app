package service.server.session.impl;

import lombok.extern.slf4j.Slf4j;
import service.server.core.base.ClientConnection;
import protocol.response.ResponseMessages;
import service.server.session.base.SessionRegistry;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SessionRegistryImpl implements SessionRegistry {

    private final ConcurrentHashMap<String, ClientConnection> activeSessions = new ConcurrentHashMap<>();

    @Override
    public void register(String username, ClientConnection connection) {
        if (username == null || connection == null) return;
        activeSessions.put(username, connection);
        log.info("Session started for user: {}", username);
    }

    @Override
    public void unregister(String username) {
        if (activeSessions.remove(username) != null) {
            log.info("Session ended for user: {}", username);
        }
    }

    @Override
    public ClientConnection getClient(String username) {
        return activeSessions.get(username);
    }

    @Override
    public boolean exists(String username) {
        return activeSessions.containsKey(username);
    }

    @Override
    public List<String> getOnlineUsers() {
        return List.copyOf(activeSessions.keySet());
    }

    @Override
    public boolean disconnect(String username) {
        ClientConnection connection = activeSessions.remove(username);
        if (connection != null) {
            connection.disconnect(ResponseMessages.DisconnectedByAdmin);
            log.info("User '{}' was disconnected by admin", username);
            return true;
        }
        return false;
    }

    @Override
    public void disconnectAll() {
        log.info("Terminating all active sessions...");
        activeSessions.forEach((user, conn) -> {
            conn.disconnect(ResponseMessages.ServerShuttingDown);
        });
        activeSessions.clear();
    }
}