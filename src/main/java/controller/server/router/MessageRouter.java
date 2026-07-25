package controller.server.router;

import controller.server.handler.DisconnectHandler;
import controller.server.handler.LoginHandler;
import controller.server.handler.LogoutHandler;
import controller.server.handler.OnlineUsersHandler;
import controller.server.handler.RequestHandler;
import controller.server.handler.SendMessageHandler;
import controller.server.handler.SignupHandler;
import exception.base.BusinessException;
import lombok.extern.slf4j.Slf4j;
import protocol.response.factory.ResponseFactory;
import protocol.request.BaseRequest;
import service.server.business.auth.AuthService;
import service.server.core.base.ClientConnection;
import service.server.core.impl.CoreServerManager;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MessageRouter {

    private final Map<Class<? extends BaseRequest>, RequestHandler<? extends BaseRequest>> handlers = new HashMap<>();
    private final ResponseFactory responseFactory;

    public MessageRouter(AuthService authService, CoreServerManager serverManager, ResponseFactory responseFactory) {
        this.responseFactory = responseFactory;
        register(new LoginHandler(authService, serverManager, responseFactory));
        register(new SignupHandler(authService, responseFactory));
        register(new SendMessageHandler(serverManager, responseFactory));
        register(new OnlineUsersHandler(serverManager, responseFactory));
        register(new DisconnectHandler(responseFactory));
        register(new LogoutHandler(serverManager, responseFactory));
    }

    public void route(BaseRequest request, ClientConnection clientConnection) throws BusinessException {
        if (request == null) {
            clientConnection.send(responseFactory.systemNotification("Message format not recognized."));
            return;
        }

        RequestHandler<? extends BaseRequest> handler = handlers.get(request.getClass());
        if (handler == null) {
            log.warn("No handler found for request type {}", request.getClass().getSimpleName());
            clientConnection.send(responseFactory.systemNotification("Unsupported request type."));
            return;
        }

        dispatch(handler, request, clientConnection);
    }

    private <T extends BaseRequest> void register(RequestHandler<T> handler) {
        handlers.put(handler.requestType(), handler);
    }

    @SuppressWarnings("unchecked")
    private <T extends BaseRequest> void dispatch(RequestHandler<? extends BaseRequest> handler, BaseRequest request, ClientConnection clientConnection) throws BusinessException {
        ((RequestHandler<T>) handler).handle((T) request, clientConnection);
    }
}
