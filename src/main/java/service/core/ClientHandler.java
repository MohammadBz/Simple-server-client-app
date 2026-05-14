package service.core;

import service.session.ConnectionRegistry;
import service.session.Session;
import protocol.message.Message;
import protocol.message.MessageType;
import exception.ConnectionException;
import exception.MessageProcessingException;
import exception.MessageRoutingException;
import handler.server.HandlerFactory;
import handler.server.MessageHandler;
import lombok.extern.slf4j.Slf4j;
import domain.chat.ChatMessage;
import infrastructure.network.ConnectionManager;
import protocol.dto.chat.IncomingMessageDTO;
import infrastructure.serialization.JsonUtil;
import protocol.message.factory.ResponseFactory;

import java.util.UUID;

@Slf4j
public class ClientHandler implements Runnable {

    private final ConnectionManager connectionManager;
    private final HandlerFactory handlerFactory;
    private final ConnectionRegistry connectionRegistry;
    ServerManager serverManager;
    private final Session session;
    private volatile boolean running = true;
    private volatile boolean closed = false;


    private final String clientId = UUID.randomUUID().toString();

    public ClientHandler(ConnectionManager connectionManager, HandlerFactory handlerFactory, ServerManager serverManager, ConnectionRegistry connectionRegistry) {
        this.connectionManager = connectionManager;
        this.handlerFactory = handlerFactory;
        this.serverManager = serverManager;
        this.connectionRegistry = connectionRegistry;
        this.session = new Session();
    }

    @Override
    public void run() {
        log.info("Client connected");
        try {
            while (running) {
                Message message = Message.fromJson(connectionManager.receive()); // unmarshall fucntion & btter to implement the flow with functions
                log.debug("Received message type: {}  from: {}", message.getType(), clientId);
                MessageHandler handler = handlerFactory.getHandler(message.getType());

                if (handler == null) {
                    log.warn("No handler for type: {}   from: {}", message.getType(), clientId);
                    send(new Message(MessageType.ERROR, "app", "Unsupported message type"));
                    continue;
                } //
                handler.handle(message, this);
            }

        } catch (ConnectionException e) {
            log.info("Client {} disconnected: {}", clientId, e.getMessage());
        } catch (MessageProcessingException e) {
            log.error("Processing error :{}", e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error in client handler, client id:{} -> {}", clientId, e.getMessage(), e);
        } finally {
            cleanup();
        }
    }

    public synchronized void send(Message message) {
        try {
            connectionManager.send(message.toJson());
        } catch (ConnectionException e) {
            log.warn("Failed to send message to client: {} , {}", clientId, e.getMessage());
        }
    }

    public void stop() {
        running = false;
        connectionManager.close();
    }

    public void deliver(ChatMessage chatMessage) throws MessageRoutingException {
        try {
            IncomingMessageDTO dto = new IncomingMessageDTO(chatMessage.getSender(), chatMessage.getContent(), chatMessage.getTimestamp());

            Message outgoing = new Message(MessageType.INCOMING_MESSAGE, "app", JsonUtil.toJson(dto));

            send(outgoing);

        } catch (Exception e) {
            log.error("Failed to deliver message : {} ", e.getMessage());
            throw new MessageRoutingException("");
        }
    }

    public Session getSession() {
        return session;
    }

    public String getClientId() {
        return clientId;
    }

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