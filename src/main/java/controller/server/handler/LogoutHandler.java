package controller.server.handler;

import service.server.core.ClientConnection;
import lombok.extern.slf4j.Slf4j;
import protocol.message.Message;
import protocol.message.factory.ResponseFactory;
import service.server.core.SessionOperations;

@Slf4j
public class LogoutHandler implements MessageHandler {

    private final SessionOperations sessionOperations;

    public LogoutHandler(SessionOperations serverManager) {
        this.sessionOperations = serverManager;
    }

    @Override
    public void handle(Message message, ClientConnection Clientconnection) {

        String username = message.getSender();

        log.info("Logging out user {}", username);

        sessionOperations.unregisterSession(username);

        Message response = ResponseFactory.logout();

        Clientconnection.send(response);
    }
}
