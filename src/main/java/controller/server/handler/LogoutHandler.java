package controller.server.handler;

import protocol.message.factory.ResponseFactory;
import service.server.core.ClientConnection;
import lombok.extern.slf4j.Slf4j;
import protocol.message.Message;

import service.server.core.SessionOperations;

@Slf4j
public class LogoutHandler implements MessageHandler {

    private final SessionOperations sessionOperations;
    private final ResponseFactory responseFactory;

    public LogoutHandler(SessionOperations serverManager, ResponseFactory responseFactory) {
        this.sessionOperations = serverManager;
        this.responseFactory = responseFactory;
    }

    @Override
    public void handle(Message message, ClientConnection Clientconnection) {

        String username = message.getSender();

        log.info("Logging out user {}", username);

        sessionOperations.unregisterSession(username);

        Message response = responseFactory.logout();

        Clientconnection.send(response);
    }
}
