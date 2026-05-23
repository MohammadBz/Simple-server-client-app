package handler.server;

import exception.LoginValidationException;
import protocol.response.ResponseMessages;
import protocol.dto.auth.LoginRequestDTO;
import exception.AuthenticationException;
import exception.MessageProcessingException;
import exception.ValidationException;
import service.auth.AuthService;
import service.core.ClientHandler;
import protocol.message.Message;
import service.core.ServerManager;
import validation.ValidationUtil;
import lombok.extern.slf4j.Slf4j;
import infrastructure.serialization.JsonUtil;
import protocol.message.factory.ResponseFactory;


@Slf4j
public class LoginHandler implements MessageHandler {

    private final AuthService authService;
    private final ServerManager serverManager;

    public LoginHandler(AuthService authService, ServerManager serverManager) {
        this.authService = authService;
        this.serverManager = serverManager;
    }

    @Override
    public void handle(Message message, ClientHandler clientHandler) throws MessageProcessingException {

        LoginRequestDTO request = JsonUtil.fromJson((String) message.getPayload(), LoginRequestDTO.class);


        log.debug("Handling LOGIN_REQUEST from {}", message.getSender());
        validateLogin(request);

        authService.login(request.getUsername(), request.getPassword());

        clientHandler.getSession().authenticate(request.getUsername());
        serverManager.registerSession(request.getUsername(), clientHandler);
        log.info("User '{}' logged in successfully", request.getUsername());

        clientHandler.send(ResponseFactory.loginSuccess());
    }

    private void validateLogin(LoginRequestDTO request) {
        try {
            ValidationUtil.validateCredentials(request.getUsername(), request.getPassword());
        } catch (ValidationException e) {
            throw new LoginValidationException(request.getUsername(), e.getMessage());
        }
    }

}