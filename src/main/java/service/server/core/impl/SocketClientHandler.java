package service.server.core.impl;

import exception.base.BusinessException;
import controller.server.router.MessageRouter;
import infrastructure.network.ConnectionManager;
import service.server.errorresolver.ServerErrorResolver;
import exception.technical.ConnectionException;
import exception.business.MessageRoutingException;
import service.server.session.base.ConnectionRegistry;
import service.server.session.impl.Session;
import lombok.extern.slf4j.Slf4j;
import domain.chat.ChatMessage;
import protocol.dto.chat.IncomingMessageDTO;
import infrastructure.marshalling.Marshaller;
import protocol.message.Message;
import protocol.message.MessageType;
import protocol.response.factory.ResponseFactory;
import protocol.request.BaseRequest;

@Slf4j
public class SocketClientHandler extends AbstractClientConnection {

    private final ConnectionManager connectionManager;
    private final Marshaller<String> marshaller;
    private final MessageRouter messageRouter;
    private final ConnectionRegistry connectionRegistry;
    private final CoreServerManager serverManager;
    private final ResponseFactory responseFactory;
    private final ServerErrorResolver serverBusinessErrorResolver;
    private final ServerErrorResolver serverSystemErrorResolver;
    private final Session session;

    public SocketClientHandler(ConnectionManager connectionManager, MessageRouter messageRouter, ConnectionRegistry connectionRegistry,
                               CoreServerManager serverManager, ResponseFactory responseFactory,
                               ServerErrorResolver serverBusinessErrorResolver, ServerErrorResolver systemErrorResolver,
                               Marshaller<String> marshaller) {
        this.connectionManager = connectionManager;
        this.messageRouter = messageRouter;
        this.connectionRegistry = connectionRegistry;
        this.serverManager = serverManager;
        this.responseFactory = responseFactory;
        this.serverBusinessErrorResolver = serverBusinessErrorResolver;
        this.serverSystemErrorResolver = systemErrorResolver;
        this.marshaller = marshaller;
        this.session = new Session();
    }

    @Override
    protected void handleCycle() throws Exception {
        String rawJson = connectionManager.receive();
        processMessage(rawJson);
    }

    private void processMessage(String rawJson) {
        try {
            BaseRequest request = marshaller.unmarshall(rawJson, BaseRequest.class);
            messageRouter.route(request, this);
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
            connectionManager.send(marshaller.marshall(message));
        } catch (ConnectionException e) {
            log.warn("Failed to send message to client: {} , {}", clientId, e.getMessage());
        }
    }

    @Override
    public void deliver(ChatMessage chatMessage) {
        try {
            IncomingMessageDTO dto = new IncomingMessageDTO(chatMessage.getSender(), chatMessage.getContent(), chatMessage.getTimestamp());

            String payload = marshaller.marshall(dto);
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
