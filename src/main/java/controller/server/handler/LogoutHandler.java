package controller.server.handler;

import lombok.extern.slf4j.Slf4j;
import protocol.message.Message;
import protocol.response.factory.ResponseFactory;
import protocol.request.LogoutRequest;
import protocol.response.factory.ResponseFactoryImpl;
import service.server.core.base.ClientConnection;
import service.server.core.base.SessionOperations;
import service.server.core.impl.CoreServerManager;

@Slf4j
public enum LogoutHandler implements RequestHandler<LogoutRequest> {
    INSTANCE;
    private final SessionOperations sessionOperations = CoreServerManager.INSTANCE;
    private final ResponseFactory responseFactory = ResponseFactoryImpl.INSTANCE;

    private LogoutHandler() {
    }

    @Override
    public Class<LogoutRequest> requestType() {
        return LogoutRequest.class;
    }

    @Override
    public void handle(LogoutRequest request, ClientConnection clientConnection) {
        String username = request.getSender();

        log.info("Logging out user {}", username);

        sessionOperations.unregisterSession(username);
        clientConnection.getSession().clear();

        Message response = responseFactory.logout();
        clientConnection.send(response);
    }
}
