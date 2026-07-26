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
import protocol.response.factory.ResponseFactoryImpl;
import service.server.business.auth.AuthService;
import service.server.business.auth.AuthServiceImpl;
import service.server.core.base.ClientConnection;
import service.server.core.impl.CoreServerManager;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public enum MessageRouter {
    INSTANCE;
    private final RouterValidator routerValidator = RouterValidator.INSTANCE;
    private final Map<Class<? extends BaseRequest>, RequestHandler<? extends BaseRequest>> handlers = new HashMap<>();


    private MessageRouter() {
        register(new LoginHandler(AuthServiceImpl.INSTANCE, CoreServerManager.INSTANCE, ResponseFactoryImpl.INSTANCE));
        register(new SignupHandler(AuthServiceImpl.INSTANCE, ResponseFactoryImpl.INSTANCE));
        register(new SendMessageHandler(CoreServerManager.INSTANCE, ResponseFactoryImpl.INSTANCE));
        register(new OnlineUsersHandler(CoreServerManager.INSTANCE, ResponseFactoryImpl.INSTANCE));
        register(new DisconnectHandler(ResponseFactoryImpl.INSTANCE));
        register(new LogoutHandler(CoreServerManager.INSTANCE, ResponseFactoryImpl.INSTANCE));
    }

    public void route(BaseRequest request, ClientConnection clientConnection) throws BusinessException {

        RequestHandler<? extends BaseRequest> handler = handlers.get(request.getClass());
        routerValidator.validateRouter(handler, request);
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
