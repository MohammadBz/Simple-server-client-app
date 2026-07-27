package service.server.errorresolver;

import exception.technical.ConnectionException;
import exception.technical.JsonUnmarshallingException;
import exception.technical.JsonMarshallingException;
import lombok.extern.slf4j.Slf4j;
import protocol.response.factory.ResponseFactory;
import protocol.response.factory.ResponseFactoryImpl;
import service.server.core.base.ClientConnection;

import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public enum ServerSystemErrorResolver implements ServerErrorResolver {
    INSTANCE;
    private final Map<Class<? extends Exception>, ErrorAction> registry = new HashMap<>();
    private final ResponseFactory responseFactory = ResponseFactoryImpl.INSTANCE;

    private ServerSystemErrorResolver() {
        register(ConnectionException.class, new ConnectionErrorAction());
        register(JsonMarshallingException.class, new JsonSerializationErrorAction());
        register(JsonUnmarshallingException.class, new JsonDeserializationErrorAction());
        register(SocketException.class, new SocketExceptionErrorAction());
        register(IOException.class, new IOExceptionErrorAction());
    }


    @Override
    public void resolve(Exception e, ClientConnection client) {
        ErrorAction action = registry.get(e.getClass());

        if (action != null) {
            action.execute(e, client);
        } else {
            log.error("CRITICAL UNKNOWN ERROR: ", e);
            if (client != null) {
                client.disconnect("Internal Server Error occurred.");
            }
        }
    }

    @FunctionalInterface
    public interface ErrorAction {
        void execute(Exception e, ClientConnection client);
    }

    private class ConnectionErrorAction implements ErrorAction {
        @Override
        public void execute(Exception e, ClientConnection client) {
            if (client == null) {
                log.error("Network connection failure occurred before client initialization: {}", e.getMessage());
                return;
            }
            log.error("Network connection failure for client {}: {}", client.getClientId(), e.getMessage());
            client.stop();
        }
    }

    private class JsonSerializationErrorAction implements ErrorAction {
        @Override
        public void execute(Exception e, ClientConnection client) {
            if (client == null) {
                log.error("Protocol Violation: Failed to marshall response. No client context available.");
                return;
            }
            log.error("Protocol Violation: Failed to marshall response for client {}", client.getClientId());
            client.disconnect("Protocol error: Server failed to format message.");
        }
    }

    private class JsonDeserializationErrorAction implements ErrorAction {
        @Override
        public void execute(Exception e, ClientConnection client) {
            if (client == null) {
                log.warn("Invalid protocol format received: {}", e.getMessage());
                return;
            }
            log.warn("Invalid protocol format from client {}: {}", client.getClientId(), e.getMessage());
            client.send(responseFactory.systemNotification("Message format not recognized."));
        }
    }

    private class SocketExceptionErrorAction implements ErrorAction {
        @Override
        public void execute(Exception e, ClientConnection client) {
            if (client == null) {
                log.warn("Socket closed unexpectedly: {}", e.getMessage());
                return;
            }
            log.warn("Socket closed unexpectedly for client: {}", e.getMessage());
            client.stop();
        }
    }

    private class IOExceptionErrorAction implements ErrorAction {
        @Override
        public void execute(Exception e, ClientConnection client) {
            log.error("IO Error during communication: {}", e.getMessage());
            if (client != null) {
                client.disconnect("Server infrastructure error.");
            }
        }
    }

    private void register(Class<? extends Exception> type, ServerSystemErrorResolver.ErrorAction action) {
        registry.put(type, action);
    }
}