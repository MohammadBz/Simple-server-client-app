package controller.server.handler;

import service.server.core.ClientConnection ;
import service.server.core.ServerManager;
import lombok.extern.slf4j.Slf4j;
import protocol.message.Message;
import protocol.message.factory.ResponseFactory;

@Slf4j
public class LogoutHandler implements MessageHandler {

    private final ServerManager serverManager;

    public LogoutHandler(ServerManager serverManager) {
        this.serverManager = serverManager;
    }

    @Override
    public void handle(Message message, ClientConnection Clientconnection) {

        String username = message.getSender();

        log.info("Logging out user {}", username);

        serverManager.unregisterSession(username);

        Message response = ResponseFactory.logout();

        Clientconnection.send(response);
    }
}
