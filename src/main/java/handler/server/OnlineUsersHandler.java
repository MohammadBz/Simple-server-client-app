package handler.server;

import domain.chat.MessageStatus;
import protocol.message.Message;
import protocol.message.factory.ResponseFactory;
import protocol.response.ResponseMessages;
import exception.AuthenticationException;
import service.core.ClientHandler;
import service.core.ServerManager;

import java.util.ArrayList;
import java.util.List;

public class OnlineUsersHandler implements MessageHandler {

    private final ServerManager serverManager;

    public OnlineUsersHandler(ServerManager serverManager) {
        this.serverManager = serverManager;
    }

    @Override
    public void handle(Message message, ClientHandler clientHandler) {

        try {
            if (!clientHandler.getSession().isAuthenticated()) {
                throw new AuthenticationException("User not authenticated.");
            }
            List<String> users = new ArrayList<>(serverManager.getOnlineUsers());

            users.remove(clientHandler.getSession().getUsername());
            clientHandler.send(ResponseFactory.onlineUsers(users));

        } catch (AuthenticationException e) {
            clientHandler.send(ResponseFactory.deliveryStatus(null, MessageStatus.FAILED, ResponseMessages.UNAUTHORIZED));
        }
    }
}