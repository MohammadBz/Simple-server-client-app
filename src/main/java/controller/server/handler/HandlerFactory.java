package controller.server.handler;

import protocol.message.MessageType;
import service.server.business.auth.AuthService;
import service.server.core.ServerManager;

import java.util.HashMap;
import java.util.Map;

public class HandlerFactory {

    private final Map<MessageType, MessageHandler> handlers = new HashMap<>();

    public HandlerFactory(AuthService authService, ServerManager serverManager) {
        handlers.put(MessageType.LOGIN_REQUEST, new LoginHandler(authService, serverManager));
        handlers.put(MessageType.SIGNUP_REQUEST, new SignupHandler(authService));
        handlers.put(MessageType.SEND_MESSAGE_REQUEST, new SendMessageHandler(serverManager));
        handlers.put(MessageType.ONLINE_USERS_REQUEST, new OnlineUsersHandler(serverManager));
        handlers.put(MessageType.DISCONNECT_REQUEST, new DisconnectHandler());
        handlers.put(MessageType.LOGOUT_REQUEST, new LogoutHandler(serverManager));
    }

    public MessageHandler getHandler(MessageType type) {
        return handlers.get(type);
    }
}