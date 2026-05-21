package handler.server;

import protocol.dto.chat.SendMessageRequestDTO;
import exception.AuthenticationException;
import exception.MessageRoutingException;
import exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import domain.chat.ChatMessage;
import domain.chat.MessageStatus;
import service.core.ClientHandler;
import protocol.message.Message;
import protocol.message.factory.ResponseFactory;
import protocol.response.ResponseMessages;
import service.core.ServerManager;
import infrastructure.serialization.JsonUtil;
import validation.MessageValidator;

@Slf4j
public class SendMessageHandler implements MessageHandler {

    private final ServerManager serverManager;

    public SendMessageHandler(ServerManager serverManager) {
        this.serverManager = serverManager;
    }

    @Override
    public void handle(Message message, ClientHandler clientHandler) {

        SendMessageRequestDTO request = JsonUtil.fromJson(message.getPayload(), SendMessageRequestDTO.class);

        try {
            if (!clientHandler.getSession().isAuthenticated()) {
                throw new AuthenticationException("User not authenticated.");
            }

            MessageValidator.validateMessage(request.getRecipient(), request.getContent());

            ChatMessage chatMessage = new ChatMessage(clientHandler.getSession().getUsername(), request.getRecipient(), request.getContent());

            ChatMessage deliveredMessage = serverManager.sendMessage(chatMessage);

            Message response = ResponseFactory.deliveryStatus(deliveredMessage.getId(), deliveredMessage.getStatus(), ResponseMessages.MESSAGE_SENT);

            clientHandler.send(response);

        } catch (ValidationException | MessageRoutingException e) {

            clientHandler.send(ResponseFactory.deliveryStatus(null, MessageStatus.FAILED, e.getMessage()));
            log.error("Failed to deliver Message", e);

        } catch (AuthenticationException e) {
            clientHandler.send(ResponseFactory.deliveryStatus(null, MessageStatus.FAILED, ResponseMessages.UNAUTHORIZED));
            log.error("Failed to deliver Message", e);
        }
    }
}