package handler.server;

import protocol.message.MessageType;
import application.auth.AuthService;
import application.core.ServerManager;

import java.util.HashMap;
import java.util.Map;

public class HandlerFactory {

    private final Map<MessageType, MessageHandler> handlers = new HashMap<>();

    public HandlerFactory(AuthService authService, ServerManager serverManager) {
        handlers.put(MessageType.LOGIN_REQUEST, new LoginHandler(authService, serverManager));
        handlers.put(MessageType.SIGNUP_REQUEST, new SignupHandler(authService, serverManager));
        handlers.put(MessageType.SEND_MESSAGE_REQUEST, new SendMessageHandler(serverManager));
        handlers.put(MessageType.ONLINE_USERS_REQUEST, new OnlineUsersHandler(serverManager));
        handlers.put(MessageType.DISCONNECT_REQUEST, new DisconnectHandler(serverManager));
        handlers.put(MessageType.LOGOUT_REQUEST, new LogoutHandler(serverManager));
    }

    public MessageHandler getHandler(MessageType type) {
        return handlers.get(type);
    }
}