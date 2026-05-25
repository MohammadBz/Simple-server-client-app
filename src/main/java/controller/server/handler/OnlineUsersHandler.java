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
    private final ResponseFactory responseFactory;

    public OnlineUsersHandler(AdminOperations serverManager, ResponseFactory responseFactory) {
        this.adminOperations = serverManager;
        this.responseFactory = responseFactory;
    }

    @Override
    public void handle(Message message, ClientConnection Clientconnection) {

        if (!Clientconnection.getSession().isAuthenticated()) {
            throw new UnauthorizedException("User not authenticated.");
        }
        List<String> users = new ArrayList<>(adminOperations.getOnlineUsers());
        users.remove(Clientconnection.getSession().getUsername());
        Clientconnection.send(responseFactory.onlineUsers(users));

    }
}