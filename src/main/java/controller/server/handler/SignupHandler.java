package controller.server.handler;

import exception.business.MessageProcessingException;
import exception.validation.SignupValidationException;
import exception.validation.ValidationException;
import infrastructure.serialization.JsonUtil;
import protocol.message.Message;
import protocol.dto.auth.SignupRequestDTO;
import lombok.extern.slf4j.Slf4j;
import service.server.business.auth.AuthService;
import service.server.core.ClientConnection;
import service.common.validation.ValidationUtil;
import protocol.message.factory.ResponseFactory;

@Slf4j
public class SignupHandler implements MessageHandler {

    private final AuthService authService;

    public SignupHandler(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void handle(Message message, ClientConnection Clientconnection) throws MessageProcessingException {

        SignupRequestDTO request = JsonUtil.fromJson((String) message.getPayload(), SignupRequestDTO.class);

        log.debug("Handling SIGNUP_REQUEST from {}", message.getSender());
        try {
            ValidationUtil.validateCredentials(request.getUsername(), request.getPassword());
        } catch (ValidationException e) {
            throw new SignupValidationException(e.getMessage(), request.getUsername());
        }
        authService.signup(request.getUsername(), request.getPassword());

        Clientconnection.getSession().authenticate(request.getUsername());

        log.info("User '{}' signed up successfully", request.getUsername());

        Clientconnection.send(ResponseFactory.signupSuccess());

    }

}