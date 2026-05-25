package controller.server.handler;

import infrastructure.serialization.Serializer;
import protocol.message.MessageType;
import service.server.business.auth.AuthService;
import service.server.core.CoreServerManager;

import java.util.HashMap;
import java.util.Map;

public class HandlerFactory {

    private final Map<MessageType, MessageHandler> handlers = new HashMap<>();

    public HandlerFactory(AuthService authService, CoreServerManager serverManager, Serializer serializer) {
        handlers.put(MessageType.LOGIN_REQUEST, new LoginHandler(authService, serverManager, serializer));
        handlers.put(MessageType.SIGNUP_REQUEST, new SignupHandler(authService, serializer));
        handlers.put(MessageType.SEND_MESSAGE_REQUEST, new SendMessageHandler(serverManager, serializer));
        handlers.put(MessageType.ONLINE_USERS_REQUEST, new OnlineUsersHandler(serverManager));
        handlers.put(MessageType.DISCONNECT_REQUEST, new DisconnectHandler());
        handlers.put(MessageType.LOGOUT_REQUEST, new LogoutHandler(serverManager));
    }

    public MessageHandler getHandler(MessageType type) {
        return handlers.get(type);
    }
}