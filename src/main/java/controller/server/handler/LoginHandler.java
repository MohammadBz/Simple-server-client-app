package controller.server.handler;

import exception.validation.LoginValidationException;
import protocol.dto.auth.LoginRequestDTO;
import exception.business.MessageProcessingException;
import exception.validation.ValidationException;
import service.server.business.auth.AuthService;
import service.server.core.ClientConnection;
import protocol.message.Message;
import service.server.core.ServerManager;
import service.common.validation.ValidationUtil;
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
    public void handle(Message message, ClientConnection Clientconnection) throws MessageProcessingException {

        LoginRequestDTO request = JsonUtil.fromJson((String) message.getPayload(), LoginRequestDTO.class);


        log.debug("Handling LOGIN_REQUEST from {}", message.getSender());
        validateLogin(request);

        authService.login(request.getUsername(), request.getPassword());

        Clientconnection.getSession().authenticate(request.getUsername());
        serverManager.registerSession(request.getUsername(), Clientconnection);
        log.info("User '{}' logged in successfully", request.getUsername());

        Clientconnection.send(ResponseFactory.loginSuccess());
    }

    private void validateLogin(LoginRequestDTO request) {
        try {
            ValidationUtil.validateCredentials(request.getUsername(), request.getPassword());
        } catch (ValidationException e) {
            throw new LoginValidationException(request.getUsername(), e.getMessage());
        }
    }

}