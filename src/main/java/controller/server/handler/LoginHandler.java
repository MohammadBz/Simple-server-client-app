package controller.server.handler;

import exception.validation.LoginValidationException;
import infrastructure.serialization.Serializer;
import protocol.dto.auth.LoginRequestDTO;
import exception.business.MessageProcessingException;
import exception.validation.ValidationException;
import service.server.business.auth.AuthService;
import service.server.core.ClientConnection;
import protocol.message.Message;
import service.common.validation.UserValidator;
import lombok.extern.slf4j.Slf4j;
import protocol.message.factory.ResponseFactory;
import service.server.core.SessionOperations;


@Slf4j
public class LoginHandler implements MessageHandler {

    private final AuthService authService;
    private final SessionOperations sessionOperations;
    private final Serializer serializer;

    public LoginHandler(AuthService authService, SessionOperations serverManager, Serializer serializer) {
        this.authService = authService;
        this.sessionOperations = serverManager;
        this.serializer = serializer;
    }

    @Override
    public void handle(Message message, ClientConnection Clientconnection) throws MessageProcessingException {

        LoginRequestDTO request = serializer.deserialize(message.getPayload(), LoginRequestDTO.class);


        log.debug("Handling LOGIN_REQUEST from {}", message.getSender());
        validateLogin(request);

        authService.login(request.getUsername(), request.getPassword());

        Clientconnection.getSession().authenticate(request.getUsername());
        sessionOperations.registerSession(request.getUsername(), Clientconnection);
        log.info("User '{}' logged in successfully", request.getUsername());

        Clientconnection.send(ResponseFactory.loginSuccess());
    }

    private void validateLogin(LoginRequestDTO request) {
        try {
            UserValidator.validateCredentials(request.getUsername(), request.getPassword());
        } catch (ValidationException e) {
            throw new LoginValidationException(request.getUsername(), e.getMessage());
        }
    }

}