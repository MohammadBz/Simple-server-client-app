package service.server.session;

import service.server.core.ClientConnection;
import lombok.extern.slf4j.Slf4j;
import protocol.response.ResponseMessages;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ConnectionRegistryImpl implements ConnectionRegistry {

    private final Set<ClientConnection> activeConnections = ConcurrentHashMap.newKeySet();

    @Override
    public void register(ClientConnection connection) {
        if (connection == null) return;
        activeConnections.add(connection);
        log.info("Connection registered. Total active: {}", activeConnections.size());
    }

    @Override
    public void unregister(ClientConnection connection) {
        if (activeConnections.remove(connection)) {
            log.info("Connection removed. Total active: {}", activeConnections.size());
        }
    }

    @Override
    public void disconnectAll() {
        log.info("Disconnecting all {} connections...", activeConnections.size());
        activeConnections.forEach(conn -> {
            try {
                conn.disconnect(ResponseMessages.ServerShuttingDown);
            } catch (Exception e) {
                log.warn("Error disconnecting client: {}", e.getMessage());
            }
        });
        activeConnections.clear();
    }

    @Override
    public int size() {
        return activeConnections.size();
    }
}