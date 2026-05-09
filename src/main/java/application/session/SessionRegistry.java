package application.session;

import lombok.extern.slf4j.Slf4j;
import application.core.ClientHandler;
import protocol.response.ResponseMessages;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SessionRegistry {

    private final ConcurrentHashMap<String, ClientHandler> activeSessions = new ConcurrentHashMap<>();

    public void register(String username, ClientHandler handler) {
        activeSessions.put(username, handler);
        log.info("Registered session for user '{}'", username);
    }

    public void unregister(String username) {
        if (username != null) {
            activeSessions.remove(username);
            log.info("Unregistered session for user '{}'", username);
        }
    }

    public ClientHandler getClient(String username) {
        return activeSessions.get(username);
    }

    public boolean exists(String username) {
        return activeSessions.containsKey(username);
    }

    public List<String> getOnlineUsers() {
        return List.copyOf(activeSessions.keySet());
    }

    public Boolean disconnect(String username) {
        if (username == null) {
            return false;
        }
        ClientHandler handler = getClient(username);
        if (handler == null) {
            return false;
        }
        handler.disconnect(ResponseMessages.DisconnectedByAdmin);
        unregister(username);
        return true;
    }

    public void disconnectAll() {
        for (ClientHandler handler : activeSessions.values()) {
            handler.disconnect(ResponseMessages.ServerShuttingDown);
        }
        activeSessions.clear();

        log.info("All sessions disconnected.");
    }
}