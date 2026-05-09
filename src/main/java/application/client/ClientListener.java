package application.client;

import exception.ConnectionException;
import protocol.dto.chat.DeliveryStatusDTO;
import protocol.dto.chat.OnlineUsersResponseDTO;
import protocol.response.ResponseDTO;
import protocol.dto.chat.IncomingMessageDTO;
import lombok.extern.slf4j.Slf4j;
import infrastructure.network.ConnectionManager;
import protocol.message.Message;
import infrastructure.serialization.JsonUtil;
import app.client.event.ClientEventHandler;


@Slf4j
public class ClientListener implements Runnable {

    private final ConnectionManager connectionManager;
    private final ClientEventHandler eventHandler;
    private volatile boolean running = true;

    public ClientListener(ConnectionManager connectionManager, ClientEventHandler clientEventHandler) {
        this.connectionManager = connectionManager;
        this.eventHandler = clientEventHandler;
    }

    @Override
    public void run() {
        try {
            while (running) {
                Message message = Message.fromJson(connectionManager.receive());
                log.debug("Received message: {}", message.getType());

                dispatch(message);
            }
        } catch (ConnectionException e) {
            if ("CONNECTION_CLOSED".equals(e.getMessage())) {
                log.info("Listener stopped: normal disconnect");
                eventHandler.onConnectionLost("Disconnected");
            } else {
                log.error("Listener stopped due to network error", e);
                eventHandler.onConnectionLost("Network error");
            }
        } catch (Exception e) {
            log.error("Listener stopped due to error : {}", e.getMessage());
            eventHandler.onConnectionLost("Network error");
        }

    }

    private void dispatch(Message message) {

        switch (message.getType()) {

            case LOGIN_RESPONSE -> handleLoginResponse(message);

            case SIGNUP_RESPONSE -> handleSignupResponse(message);

            case INCOMING_MESSAGE -> handleIncomingMessage(message);

            case DELIVERY_STATUS -> handleDeliveryStatus(message);

            case ONLINE_USERS_RESPONSE -> handleOnlineUsers(message);

            case DISCONNECT_RESPONSE -> handleDisconnect(message);

            case LOGOUT_RESPONSE -> handleLogOutResponse(message);

            case SYSTEM_NOTIFICATION -> handleSystemNotification(message);
            default -> log.warn("Unknown message type: {}", message.getType());
        }
    }

    private void handleLoginResponse(Message message) {

        ResponseDTO response = JsonUtil.fromJson((String) message.getPayload(), ResponseDTO.class);
        log.info("Processing login response");
        eventHandler.onLoginResponse(response);
    }

    private void handleSystemNotification(Message message) {
        ResponseDTO response = JsonUtil.fromJson((String) message.getPayload(), ResponseDTO.class);
        log.info("Processing system notification");
        eventHandler.onSystemNotifications(response);
    }

    private void handleLogOutResponse(Message message) {
        ResponseDTO response = JsonUtil.fromJson((String) message.getPayload(), ResponseDTO.class);
        log.info("Processing logout response");
        eventHandler.onLogoutResponse(response);
    }

    private void handleSignupResponse(Message message) {

        ResponseDTO response = JsonUtil.fromJson((String) message.getPayload(), ResponseDTO.class);
        log.info("Processing sign up response");
        eventHandler.onSignupResponse(response);

    }

    private void handleIncomingMessage(Message message) {
        IncomingMessageDTO dto = JsonUtil.fromJson((String) message.getPayload(), IncomingMessageDTO.class);
        log.info("Incoming message received from {}", dto.getSender());
        eventHandler.onIncomingMessage(dto);
    }

    private void handleDeliveryStatus(Message message) {
        DeliveryStatusDTO dto = JsonUtil.fromJson((String) message.getPayload(), DeliveryStatusDTO.class);
        log.info("Handling delivery status");
        eventHandler.onDeliveryStatus(dto);
    }

    private void handleOnlineUsers(Message message) {
        OnlineUsersResponseDTO dto = JsonUtil.fromJson((String) message.getPayload(), OnlineUsersResponseDTO.class);
        log.info("Handling online users response");
        eventHandler.onOnlineUsers(dto);
    }

    private void handleDisconnect(Message message) {
        ResponseDTO response = JsonUtil.fromJson((String) message.getPayload(), ResponseDTO.class);
        log.info("Handling disconnect response");
        eventHandler.onDisconnectResponse(response);
    }

    public void stop() {
        running = false;
    }
}