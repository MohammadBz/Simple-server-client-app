package controller.server.handler;

import domain.chat.ChatMessage;
import exception.business.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import protocol.message.Message;
import protocol.response.factory.ResponseFactory;
import protocol.request.SendMessageRequest;
import protocol.response.ResponseMessages;
import protocol.response.factory.ResponseFactoryImpl;
import service.common.validation.MessageValidator;
import service.server.core.base.ClientConnection;
import service.server.core.base.MessageOperations;
import service.server.core.impl.CoreServerManager;

@Slf4j
public enum SendMessageHandler implements RequestHandler<SendMessageRequest> {
    INSTANCE;
    private final MessageOperations messageOperations = CoreServerManager.INSTANCE;
    private final ResponseFactory responseFactory = ResponseFactoryImpl.INSTANCE;

    private SendMessageHandler() {
    }

    @Override
    public Class<SendMessageRequest> requestType() {
        return SendMessageRequest.class;
    }

    @Override
    public void handle(SendMessageRequest request, ClientConnection clientConnection) {
        if (!clientConnection.getSession().isAuthenticated()) {
            throw new UnauthorizedException("User not authenticated.");
        }

        MessageValidator.validateMessage(request.getRecipient(), request.getContent());

        ChatMessage chatMessage = new ChatMessage(
                clientConnection.getSession().getUsername(),
                request.getRecipient(),
                request.getContent()
        );

        ChatMessage deliveredMessage = messageOperations.sendMessage(chatMessage);
        Message response = responseFactory.deliveryStatus(
                deliveredMessage.getId(),
                deliveredMessage.getStatus(),
                ResponseMessages.MESSAGE_SENT
        );

        clientConnection.send(response);
    }
}
