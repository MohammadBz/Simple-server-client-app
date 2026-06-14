package controller.server.handler;

import domain.chat.ChatMessage;
import exception.business.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import protocol.message.Message;
import protocol.response.factory.ResponseFactory;
import protocol.request.SendMessageRequest;
import protocol.response.ResponseMessages;
import service.common.validation.MessageValidator;
import service.server.core.ClientConnection;
import service.server.core.MessageOperations;

@Slf4j
public class SendMessageHandler implements RequestHandler<SendMessageRequest> {

    private final MessageOperations messageOperations;
    private final ResponseFactory responseFactory;

    public SendMessageHandler(MessageOperations messageOperations, ResponseFactory responseFactory) {
        this.messageOperations = messageOperations;
        this.responseFactory = responseFactory;
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
