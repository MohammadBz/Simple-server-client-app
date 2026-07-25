package service.client.impl;

import protocol.dto.chat.DeliveryStatusDTO;
import protocol.dto.chat.OnlineUsersResponseDTO;
import protocol.message.Message;
import infrastructure.serialization.Serializer;
import launcher.client.event.ClientEventHandler;
import lombok.extern.slf4j.Slf4j;
import protocol.message.MessageType;
import protocol.response.ResponseDTO;
import protocol.dto.chat.IncomingMessageDTO;
import service.client.base.ResponseProcessor;

import java.util.EnumMap;
import java.util.Map;

@Slf4j
public class ResponseDispatcher {
    private final Map<MessageType, ResponseProcessor> processors = new EnumMap<>(MessageType.class);
    private final Serializer serializer;
    private ClientEventHandler eventHandler;

    public ResponseDispatcher(Serializer serializer, ClientEventHandler eventHandler) {
        this.serializer = serializer;
        this.eventHandler = eventHandler;
        initializeProcessors();
    }

    private void initializeProcessors() {
        processors.put(MessageType.LOGIN_RESPONSE, msg -> {
            ResponseDTO dto = serializer.deserialize(msg.getPayload(), ResponseDTO.class);
            log.info("Successfully processed Login Response: {}", dto.getMessage());
            eventHandler.onLoginResponse(dto);
        });

        processors.put(MessageType.INCOMING_MESSAGE, msg -> {
            IncomingMessageDTO dto = serializer.deserialize(msg.getPayload(), IncomingMessageDTO.class);
            log.info("Incoming message received from [{}]", dto.getSender());
            eventHandler.onIncomingMessage(dto);
        });
        processors.put(MessageType.LOGOUT_RESPONSE, msg -> {
            ResponseDTO response = serializer.deserialize(msg.getPayload(), ResponseDTO.class);
            log.info("Processing logout response");
            eventHandler.onLogoutResponse(response);
        });
        processors.put(MessageType.SIGNUP_RESPONSE, msg -> {
            ResponseDTO response = serializer.deserialize(msg.getPayload(), ResponseDTO.class);
            log.info("Processing sign up response");
            eventHandler.onSignupResponse(response);
        });
        processors.put(MessageType.DELIVERY_STATUS, msg -> {
            DeliveryStatusDTO dto = serializer.deserialize(msg.getPayload(), DeliveryStatusDTO.class);
            log.info("Handling delivery status");
            eventHandler.onDeliveryStatus(dto);
        });
        processors.put(MessageType.ONLINE_USERS_RESPONSE, msg -> {
            OnlineUsersResponseDTO dto = serializer.deserialize(msg.getPayload(), OnlineUsersResponseDTO.class);
            log.info("Handling online users response");
            eventHandler.onOnlineUsers(dto);
        });
        processors.put(MessageType.DISCONNECT_RESPONSE, msg -> {
            ResponseDTO response = serializer.deserialize(msg.getPayload(), ResponseDTO.class);
            log.info("Handling disconnect response");
            eventHandler.onDisconnectResponse(response);
        });
        processors.put(MessageType.SYSTEM_NOTIFICATION, msg -> {
            ResponseDTO response = serializer.deserialize(msg.getPayload(), ResponseDTO.class);
            log.info("Processing system notification");
            eventHandler.onSystemNotifications(response);
        });

    }

    public void dispatch(Message message) {
        ResponseProcessor processor = processors.get(message.getType());
        if (processor != null) {
            processor.process(message);
        } else {
            log.warn("No processor found for message type: {}", message.getType());
        }
    }

}
