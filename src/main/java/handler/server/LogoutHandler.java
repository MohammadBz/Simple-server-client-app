package handler.server;

import application.core.ClientHandler;
import application.core.ServerManager;
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
    public void handle(Message message, ClientHandler clientHandler) {

        String username = message.getSender();

        log.info("Logging out user {}", username);

        serverManager.unregisterSession(username);

        Message response = ResponseFactory.logout();

        clientHandler.send(response);
    }
}
