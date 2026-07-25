package controller.server.handler;

import lombok.extern.slf4j.Slf4j;
import protocol.message.Message;
import protocol.response.factory.ResponseFactory;
import protocol.request.LogoutRequest;
import service.server.core.base.ClientConnection;
import service.server.core.base.SessionOperations;

@Slf4j
public class LogoutHandler implements RequestHandler<LogoutRequest> {

    private final SessionOperations sessionOperations;
    private final ResponseFactory responseFactory;

    public LogoutHandler(SessionOperations sessionOperations, ResponseFactory responseFactory) {
        this.sessionOperations = sessionOperations;
        this.responseFactory = responseFactory;
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
