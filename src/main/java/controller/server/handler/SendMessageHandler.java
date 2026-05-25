package controller.server.handler;

import exception.business.UnauthorizedException;
import protocol.dto.chat.SendMessageRequestDTO;
import lombok.extern.slf4j.Slf4j;
import domain.chat.ChatMessage;
import service.server.core.ClientConnection;
import protocol.message.Message;
import protocol.message.factory.ResponseFactory;
import protocol.response.ResponseMessages;
import service.server.core.MessageOperations;
import infrastructure.serialization.JsonUtil;
import service.common.validation.MessageValidator;


@Slf4j
public class SendMessageHandler implements MessageHandler {

    private final MessageOperations messageOperations;

    public SendMessageHandler(MessageOperations serverManager) {
        this.messageOperations = serverManager;
    }

    @Override
    public void handle(Message message, ClientConnection Clientconnection) {

        SendMessageRequestDTO request = JsonUtil.fromJson(message.getPayload(), SendMessageRequestDTO.class);

        if (!Clientconnection.getSession().isAuthenticated()) {
            throw new UnauthorizedException("User not authenticated.");
        }

        MessageValidator.validateMessage(request.getRecipient(), request.getContent());

        ChatMessage chatMessage = new ChatMessage(Clientconnection.getSession().getUsername(), request.getRecipient(), request.getContent());

        ChatMessage deliveredMessage = messageOperations.sendMessage(chatMessage);

        Message response = ResponseFactory.deliveryStatus(deliveredMessage.getId(), deliveredMessage.getStatus(), ResponseMessages.MESSAGE_SENT);

        Clientconnection.send(response);


    }
}