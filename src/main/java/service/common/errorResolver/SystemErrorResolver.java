package service.common.errorResolver;

import exception.technical.ConnectionException;
import exception.technical.JsonDeserializationException;
import exception.technical.JsonSerializationException;
import lombok.extern.slf4j.Slf4j;
import protocol.message.factory.ResponseFactory;
import service.server.core.ClientConnection;


import java.util.HashMap;
import java.util.Map;

@Slf4j
public class SystemErrorResolver implements ErrorResolver {
    private final Map<Class<? extends Exception>, ErrorAction> registry = new HashMap<>();

    public SystemErrorResolver() {
        register(ConnectionException.class, new ConnectionErrorAction());
        register(JsonSerializationException.class, new JsonSerializationErrorAction());
        register(JsonDeserializationException.class, new JsonDeserializationErrorAction());
    }

    @Override
    public void resolve(Exception e, ClientConnection client) {
        ErrorAction action = registry.get(e.getClass());

        if (action != null) {
            action.execute(e, client);
        } else {
            log.error("CRITICAL UNKNOWN ERROR: ", e);
            client.disconnect("Internal Server Error occurred.");
        }
    }

    @FunctionalInterface
    public interface ErrorAction {
        void execute(Exception e, ClientConnection client);
    }

    private class ConnectionErrorAction implements ErrorAction {
        @Override
        public void execute(Exception e, ClientConnection client) {
            log.error("Network connection failure for client {}: {}", client.getClientId(), e.getMessage());
            client.stop();
        }
    }

    private class JsonSerializationErrorAction implements ErrorAction {
        @Override
        public void execute(Exception e, ClientConnection client) {
            log.error("Protocol Violation: Failed to serialize response for client {}", client.getClientId());
            client.disconnect("Protocol error: Server failed to format message.");
        }
    }

    private class JsonDeserializationErrorAction implements ErrorAction {
        @Override
        public void execute(Exception e, ClientConnection client) {
            log.warn("Invalid protocol format from client {}: {}", client.getClientId(), e.getMessage());
            client.send(ResponseFactory.systemNotification("Message format not recognized."));
        }
    }

    private void register(Class<? extends Exception> type, SystemErrorResolver.ErrorAction action) {
        registry.put(type, action);
    }
}