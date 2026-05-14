package service.core;

import service.admin.AdminOperations;
import service.messaging.RoutingService;
import service.session.SessionRegistry;
import exception.MessageRoutingException;
import lombok.extern.slf4j.Slf4j;
import domain.chat.ChatMessage;

import java.util.List;

@Slf4j
public class ServerManager implements AdminOperations {

    private final SessionRegistry sessionRegistry;
    private final RoutingService routingService;
    private ShutdownCapable shutdownCapable;

    public ServerManager() {
        this.sessionRegistry = new SessionRegistry();
        this.routingService = new RoutingService(sessionRegistry);
    }

    public void registerSession(String username, ClientHandler handler) {
        sessionRegistry.register(username, handler);
    }

    public void unregisterSession(String username) {
        sessionRegistry.unregister(username);
    }

    public ChatMessage sendMessage(ChatMessage chatMessage) throws MessageRoutingException {
        return routingService.route(chatMessage);
    }

    public void setShutdownCapable(ShutdownCapable shutdownCapable) {
        this.shutdownCapable = shutdownCapable;
    }

    @Override
    public List<String> getOnlineUsers() {
        return sessionRegistry.getOnlineUsers();
    }

    @Override
    public void shutdownServer() {
        sessionRegistry.disconnectAll();

        if (shutdownCapable != null) {
            shutdownCapable.shutdown();
        }
    }

    @Override
    public boolean disconnectUser(String username) {
        return sessionRegistry.disconnect(username);
    }
    

    public boolean isOnline(String username) {
        return sessionRegistry.exists(username);
    }


}