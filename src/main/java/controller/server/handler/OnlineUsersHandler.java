package controller.server.handler;

import exception.business.UnauthorizedException;
import protocol.message.Message;
import protocol.message.factory.ResponseFactory;
import service.server.business.admin.AdminOperations;
import service.server.core.ClientConnection;

import java.util.ArrayList;
import java.util.List;

public class OnlineUsersHandler implements MessageHandler {

    private final AdminOperations adminOperations;

    public OnlineUsersHandler(AdminOperations serverManager) {
        this.adminOperations = serverManager;
    }

    @Override
    public void handle(Message message, ClientConnection Clientconnection) {

        if (!Clientconnection.getSession().isAuthenticated()) {
            throw new UnauthorizedException("User not authenticated.");
        }
        List<String> users = new ArrayList<>(adminOperations.getOnlineUsers());
        users.remove(Clientconnection.getSession().getUsername());
        Clientconnection.send(ResponseFactory.onlineUsers(users));

    }
}