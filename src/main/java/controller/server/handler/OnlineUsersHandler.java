package controller.server.handler;

import exception.business.UnauthorizedException;
import protocol.message.Message;
import protocol.message.factory.ResponseFactory;
import service.server.core.ClientHandler;
import service.server.core.ServerManager;

import java.util.ArrayList;
import java.util.List;

public class OnlineUsersHandler implements MessageHandler {

    private final ServerManager serverManager;

    public OnlineUsersHandler(ServerManager serverManager) {
        this.serverManager = serverManager;
    }

    @Override
    public void handle(Message message, ClientHandler clientHandler) {

        if (!clientHandler.getSession().isAuthenticated()) {
            throw new UnauthorizedException("User not authenticated.");
        }
        List<String> users = new ArrayList<>(serverManager.getOnlineUsers());
        users.remove(clientHandler.getSession().getUsername());
        clientHandler.send(ResponseFactory.onlineUsers(users));

    }
}