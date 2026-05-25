package controller.server.handler;

import infrastructure.serialization.Serializer;
import protocol.message.MessageType;
import protocol.message.factory.ResponseFactory;
import service.server.business.auth.AuthService;
import service.server.core.CoreServerManager;

import java.util.HashMap;
import java.util.Map;

public class HandlerFactory {

    private final Map<MessageType, MessageHandler> handlers = new HashMap<>();

    public HandlerFactory(AuthService authService, CoreServerManager serverManager, Serializer serializer, ResponseFactory responseFactory) {
        handlers.put(MessageType.LOGIN_REQUEST, new LoginHandler(authService, serverManager, serializer, responseFactory));
        handlers.put(MessageType.SIGNUP_REQUEST, new SignupHandler(authService, serializer, responseFactory));
        handlers.put(MessageType.SEND_MESSAGE_REQUEST, new SendMessageHandler(serverManager, serializer, responseFactory));
        handlers.put(MessageType.ONLINE_USERS_REQUEST, new OnlineUsersHandler(serverManager, responseFactory));
        handlers.put(MessageType.DISCONNECT_REQUEST, new DisconnectHandler(responseFactory));
        handlers.put(MessageType.LOGOUT_REQUEST, new LogoutHandler(serverManager, responseFactory));
    }

    public MessageHandler getHandler(MessageType type) {
        return handlers.get(type);
    }
}