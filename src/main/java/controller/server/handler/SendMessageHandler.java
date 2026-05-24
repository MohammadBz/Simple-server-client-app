package controller.server.handler;

import exception.business.UnauthorizedException;
import protocol.dto.chat.SendMessageRequestDTO;
import lombok.extern.slf4j.Slf4j;
import domain.chat.ChatMessage;
import service.server.core.ClientHandler;
import protocol.message.Message;
import protocol.message.factory.ResponseFactory;
import protocol.response.ResponseMessages;
import service.server.core.ServerManager;
import infrastructure.serialization.JsonUtil;
import service.common.validation.MessageValidator;


@Slf4j
public class SendMessageHandler implements MessageHandler {

    private final ServerManager serverManager;

    public SendMessageHandler(ServerManager serverManager) {
        this.serverManager = serverManager;
    }

    @Override
    public void handle(Message message, ClientHandler clientHandler) {

        SendMessageRequestDTO request = JsonUtil.fromJson(message.getPayload(), SendMessageRequestDTO.class);

        if (!clientHandler.getSession().isAuthenticated()) {
            throw new UnauthorizedException("User not authenticated.");
        }

        MessageValidator.validateMessage(request.getRecipient(), request.getContent());

        ChatMessage chatMessage = new ChatMessage(clientHandler.getSession().getUsername(), request.getRecipient(), request.getContent());

        ChatMessage deliveredMessage = serverManager.sendMessage(chatMessage);

        Message response = ResponseFactory.deliveryStatus(deliveredMessage.getId(), deliveredMessage.getStatus(), ResponseMessages.MESSAGE_SENT);

        clientHandler.send(response);


    }
}