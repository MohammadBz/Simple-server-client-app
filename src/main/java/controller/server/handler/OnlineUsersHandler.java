package controller.server.handler;

import exception.business.UnauthorizedException;
import protocol.response.factory.ResponseFactory;
import protocol.request.OnlineUsersRequest;
import protocol.response.factory.ResponseFactoryImpl;
import service.server.business.admin.AdminOperations;
import service.server.core.base.ClientConnection;
import service.server.core.impl.CoreServerManager;

import java.util.ArrayList;
import java.util.List;

public enum OnlineUsersHandler implements RequestHandler<OnlineUsersRequest> {
    INSTANCE;
    private final AdminOperations adminOperations = CoreServerManager.INSTANCE;
    private final ResponseFactory responseFactory = ResponseFactoryImpl.INSTANCE;

    private OnlineUsersHandler() {
    }

    @Override
    public Class<OnlineUsersRequest> requestType() {
        return OnlineUsersRequest.class;
    }

    @Override
    public void handle(OnlineUsersRequest request, ClientConnection clientConnection) {
        if (!clientConnection.getSession().isAuthenticated()) {
            throw new UnauthorizedException("User not authenticated.");
        }

        List<String> users = new ArrayList<>(adminOperations.getOnlineUsers());
        users.remove(clientConnection.getSession().getUsername());
        clientConnection.send(responseFactory.onlineUsers(users));
    }
}
