package service.server.core;

import exception.base.BusinessException;
import exception.business.MessageRoutingException;
import infrastructure.network.ConnectionManager;
import protocol.message.factory.ResponseFactory;
import service.server.errorResolver.ServerErrorResolver;
import exception.technical.ConnectionException;
import service.server.session.ConnectionRegistry;
import service.server.session.Session;
import protocol.message.Message;
import protocol.message.MessageType;
import controller.server.handler.HandlerFactory;
import controller.server.handler.MessageHandler;
import lombok.extern.slf4j.Slf4j;
import domain.chat.ChatMessage;
import protocol.dto.chat.IncomingMessageDTO;
import infrastructure.serialization.Serializer;

import java.util.UUID;

@Slf4j
public class SocketClientHandler extends AbstractClientConnection {

    private final ConnectionManager connectionManager;
    private final Serializer serializer;
    private final HandlerFactory handlerFactory;
    private final ConnectionRegistry connectionRegistry;
    private final ServerErrorResolver serverBusinessErrorResolver;
    private final ServerErrorResolver serverSystemErrorResolver;
    private final ResponseFactory responseFactory;
    CoreServerManager serverManager;
    private final Session session;

    public SocketClientHandler(ConnectionManager connectionManager, HandlerFactory handlerFactory, CoreServerManager serverManager, ConnectionRegistry connectionRegistry, ServerErrorResolver serverBusinessErrorResolver,
                               ServerErrorResolver systemErrorResolver, Serializer serializer, ResponseFactory responseFactory) {
        this.connectionManager = connectionManager;
        this.handlerFactory = handlerFactory;
        this.serverManager = serverManager;
        this.connectionRegistry = connectionRegistry;
        this.serverBusinessErrorResolver = serverBusinessErrorResolver;
        this.serverSystemErrorResolver = systemErrorResolver;
        this.serializer = serializer;
        this.responseFactory = responseFactory;
        this.session = new Session();
    }

    @Override
    protected void handleCycle() throws Exception {
        String rawJson = connectionManager.receive();
        processMessage(rawJson);
    }

    private void processMessage(String rawJson) {
        try {
            Message message = serializer.deserialize(rawJson, Message.class);
            MessageHandler handler = handlerFactory.getHandler(message.getType());
            if (handler == null) {
                this.send(responseFactory.systemNotification("Unsupported message type"));
                return;
            }
            handler.handle(message, this);
        } catch (BusinessException e) {
            serverBusinessErrorResolver.resolve(e, this);
        }
    }

    @Override
    protected void onCleanup() {
        connectionManager.close();
        connectionRegistry.unregister(this);
        if (session.isAuthenticated()) {
            serverManager.unregisterSession(session.getUsername());
        }
        log.info("Client {} resources released.", clientId);
    }

    @Override
    public synchronized void send(Message message) {
        try {
            connectionManager.send(serializer.serialize(message));
        } catch (ConnectionException e) {
            log.warn("Failed to send message to client: {} , {}", clientId, e.getMessage());
        }
    }

    @Override
    public void deliver(ChatMessage chatMessage) {
        try {
            IncomingMessageDTO dto = new IncomingMessageDTO(chatMessage.getSender(), chatMessage.getContent(), chatMessage.getTimestamp());

            String payload = serializer.serialize(dto);
            Message outgoing = new Message(MessageType.INCOMING_MESSAGE, "SERVER", payload);

            send(outgoing);
            log.debug("Delivered message from {} to {}", chatMessage.getSender(), clientId);

        } catch (Exception e) {
            log.error("Failed to deliver message to client {}: {}", clientId, e.getMessage());
            throw new MessageRoutingException("Delivery failed for client " + clientId, e);
        }
    }

    @Override
    protected void handleSystemError(Exception e) {
        log.error("Critical system error in handler for {}: {}", clientId, e.getMessage(), e);
        serverSystemErrorResolver.resolve(e, this);
    }

    @Override
    public Session getSession() {
        return session;
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public void disconnect(String reason) {
        try {
            Message notification = responseFactory.systemNotification(reason);
            send(notification);
            log.info("Disconnecting client {} - Reason: {}", clientId, reason);
        } catch (Exception e) {
            log.warn("Could not send disconnect notification to client {}", clientId);
        } finally {
            this.close();
        }
    }

    @Override
    public void stop() {
        log.info("Manual stop requested for client {}", clientId);
        this.close();
    }

}