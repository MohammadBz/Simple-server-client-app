package service.session;

import service.core.ClientHandler;
import lombok.extern.slf4j.Slf4j;
import protocol.response.ResponseMessages;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ConnectionRegistry {

    private final Set<ClientHandler> activeConnections = ConcurrentHashMap.newKeySet();

    public void register(ClientHandler handler) {
        activeConnections.add(handler);
        log.info("Connection registered. client id: {}", handler.getClientId());
    }

    public void unregister(ClientHandler handler) {
        if (activeConnections.contains(handler)) {
            activeConnections.remove(handler);
            log.info("Connection removed. client id: {}", handler.getClientId());
        }
    }

    public void disconnectAll() {
        for (ClientHandler handler : activeConnections) {
            handler.disconnect(ResponseMessages.ServerShuttingDown);
        }
        activeConnections.clear();
        log.info("All connections disconnected.");
    }

    public int size() {
        return activeConnections.size();
    }
}