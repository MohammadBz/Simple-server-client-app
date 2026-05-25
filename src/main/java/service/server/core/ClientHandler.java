package service.server.core;

import exception.base.BusinessException;
import exception.business.MessageRoutingException;
import service.server.errorResolver.ServerBusinessErrorResolver;
import service.server.errorResolver.ServerSystemErrorResolver;
import exception.technical.ConnectionException;
import service.server.session.ConnectionRegistry;
import service.server.session.Session;
import protocol.message.Message;
import protocol.message.MessageType;
import controller.server.handler.HandlerFactory;
import controller.server.handler.MessageHandler;
import lombok.extern.slf4j.Slf4j;
import domain.chat.ChatMessage;
import infrastructure.network.ConnectionManager;
import protocol.dto.chat.IncomingMessageDTO;
import infrastructure.serialization.JsonUtil;
import protocol.message.factory.ResponseFactory;

import java.util.UUID;

@Slf4j
public class ClientHandler implements Runnable, ClientConnection {

    private final ConnectionManager connectionManager;
    private final HandlerFactory handlerFactory;
    private final ConnectionRegistry connectionRegistry;
    private final ServerBusinessErrorResolver serverBusinessErrorResolver;
    private final ServerSystemErrorResolver systemErrorResolver;
    ServerManager serverManager;
    private final Session session;
    private volatile boolean running = true;
    private volatile boolean closed = false;


    private final String clientId = UUID.randomUUID().toString();

    public ClientHandler(ConnectionManager connectionManager, HandlerFactory handlerFactory, ServerManager serverManager, ConnectionRegistry connectionRegistry, ServerBusinessErrorResolver serverBusinessErrorResolver,
                         ServerSystemErrorResolver systemErrorResolver) {
        this.connectionManager = connectionManager;
        this.handlerFactory = handlerFactory;
        this.serverManager = serverManager;
        this.connectionRegistry = connectionRegistry;
        this.serverBusinessErrorResolver = serverBusinessErrorResolver;
        this.systemErrorResolver = systemErrorResolver;
        this.session = new Session();
    }

    @Override
    public void run() {
        log.info("Client connected: {}", clientId);
        try {
            while (running) {
                String rawJson = connectionManager.receive();
                processMessage(rawJson);
            }
        } catch (ConnectionException e) {
            systemErrorResolver.resolve(e, this);
        } catch (Exception e) {
            log.error("Fatal error in client loop for {}: {}", clientId, e.getMessage(), e);
        } finally {
            cleanup();
        }
    }

    private void processMessage(String rawJson) {
        try {
            Message message = JsonUtil.fromJson(rawJson, Message.class);

            log.debug("Received message type: {} from: {}", message.getType(), clientId);

            MessageHandler handler = handlerFactory.getHandler(message.getType());
            if (handler == null) {
                log.warn("No handler found for type: {} from: {}", message.getType(), clientId);
                this.send(ResponseFactory.systemNotification("Unsupported message type"));
                return;
            }
            handler.handle(message, this);

        } catch (BusinessException e) {
            serverBusinessErrorResolver.resolve(e, this);
        } catch (Exception e) {
            systemErrorResolver.resolve(e, this);
        }
    }

    @Override
    public synchronized void send(Message message) {
        try {
            connectionManager.send(message.toJson());
        } catch (ConnectionException e) {
            log.warn("Failed to send message to client: {} , {}", clientId, e.getMessage());
        }
    }

    public void stop() {
        running = false;
        this.close();
    }

    @Override
    public void deliver(ChatMessage chatMessage) {
        try {
            IncomingMessageDTO dto = new IncomingMessageDTO(chatMessage.getSender(), chatMessage.getContent(), chatMessage.getTimestamp());

            Message outgoing = new Message(MessageType.INCOMING_MESSAGE, "launcher", JsonUtil.toJson(dto));

            send(outgoing);

        } catch (Exception e) {
            log.error("Failed to deliver message : {} ", e.getMessage());
            throw new MessageRoutingException("Unknown error when delivering message", e);
        }
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
            connectionManager.send(ResponseFactory.systemNotification(reason).toJson());
        } catch (Exception e) {
            log.warn("Failed to notify client before disconnect");
        } finally {
            close();
        }
    }

    public void close() {
        this.cleanup();
    }

    private void cleanup() {
        if (closed) return;
        log.info("Cleaning up client {} resources", clientId);

        connectionManager.close();
        connectionRegistry.unregister(this);

        if (session.isAuthenticated()) {
            serverManager.unregisterSession(session.getUsername());
            log.info("User '{}' disconnected client id:{}", session.getUsername(), clientId);
        }
    }

}