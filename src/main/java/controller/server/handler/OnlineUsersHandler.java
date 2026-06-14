package controller.server.handler;

import exception.business.UnauthorizedException;
import protocol.response.factory.ResponseFactory;
import protocol.request.OnlineUsersRequest;
import service.server.business.admin.AdminOperations;
import service.server.core.ClientConnection;

import java.util.ArrayList;
import java.util.List;

public class OnlineUsersHandler implements RequestHandler<OnlineUsersRequest> {

    private final AdminOperations adminOperations;
    private final ResponseFactory responseFactory;

    public OnlineUsersHandler(AdminOperations adminOperations, ResponseFactory responseFactory) {
        this.adminOperations = adminOperations;
        this.responseFactory = responseFactory;
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
