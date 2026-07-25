package service.server.core.impl;

import service.server.business.admin.AdminOperations;
import service.server.business.chatmessageroute.ChatMessageRouter;
import service.server.business.chatmessageroute.ChatMessageRouterImpl;
import service.server.core.base.ClientConnection;
import service.server.core.base.MessageOperations;
import service.server.core.base.SessionOperations;
import service.server.core.base.ShutdownCapable;
import service.server.session.base.SessionRegistry;
import service.server.session.impl.SessionRegistryImpl;
import lombok.extern.slf4j.Slf4j;
import domain.chat.ChatMessage;

import java.util.List;

@Slf4j
public class CoreServerManager implements AdminOperations, MessageOperations, SessionOperations {

    private final SessionRegistry sessionRegistry;
    private final ChatMessageRouter chatMessageRouter;
    private ShutdownCapable shutdownCapable;

    public CoreServerManager() {
        this.sessionRegistry = new SessionRegistryImpl();
        this.chatMessageRouter = new ChatMessageRouterImpl(sessionRegistry);
    }

    @Override
    public void registerSession(String username, ClientConnection Clientconnection) {
        sessionRegistry.register(username, Clientconnection);
    }

    @Override
    public void unregisterSession(String username) {
        sessionRegistry.unregister(username);
    }

    @Override
    public ChatMessage sendMessage(ChatMessage chatMessage) {
        return chatMessageRouter.route(chatMessage);
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


    @Override
    public boolean isOnline(String username) {
        return sessionRegistry.exists(username);
    }


}
