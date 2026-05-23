package handler.server;

import exception.AuthenticationException;
import exception.MessageProcessingException;
import exception.SignupValidationException;
import exception.ValidationException;
import service.core.ServerManager;
import infrastructure.serialization.JsonUtil;
import protocol.message.Message;
import protocol.response.ResponseMessages;
import protocol.dto.auth.SignupRequestDTO;
import lombok.extern.slf4j.Slf4j;
import service.auth.AuthService;
import service.core.ClientHandler;
import validation.ValidationUtil;
import protocol.message.factory.ResponseFactory;

@Slf4j
public class SignupHandler implements MessageHandler {

    private final AuthService authService;

    public SignupHandler(AuthService authService, ServerManager serverManage) {
        this.authService = authService;
    }

    @Override
    public void handle(Message message, ClientHandler clientHandler) throws MessageProcessingException {

        SignupRequestDTO request = JsonUtil.fromJson((String) message.getPayload(), SignupRequestDTO.class);

        log.debug("Handling SIGNUP_REQUEST from {}", message.getSender());
        try {
            ValidationUtil.validateCredentials(request.getUsername(), request.getPassword());
        } catch (ValidationException e) {
            throw new SignupValidationException(e.getMessage(), request.getUsername());
        }
        authService.signup(request.getUsername(), request.getPassword());

        clientHandler.getSession().authenticate(request.getUsername());

        log.info("User '{}' signed up successfully", request.getUsername());

        clientHandler.send(ResponseFactory.signupSuccess());

    }

}