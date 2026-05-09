package handler.server;

import exception.AuthenticationException;
import exception.MessageProcessingException;
import exception.ValidationException;
import application.core.ServerManager;
import infrastructure.serialization.JsonUtil;
import protocol.message.Message;
import protocol.response.ResponseMessages;
import protocol.dto.auth.SignupRequestDTO;
import lombok.extern.slf4j.Slf4j;
import application.auth.AuthService;
import application.core.ClientHandler;
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

        try {
            log.debug("Handling SIGNUP_REQUEST from {}", message.getSender());

            ValidationUtil.validateCredentials(request.getUsername(), request.getPassword());

            authService.signup(request.getUsername(), request.getPassword());

            clientHandler.getSession().authenticate(request.getUsername());

            log.info("User '{}' signed up successfully", request.getUsername());

            clientHandler.send(ResponseFactory.signupSuccess());

        } catch (ValidationException e) {
            log.warn("Invalid signup input from {}: {}", message.getSender(), e.getMessage());
            clientHandler.send(ResponseFactory.signupFailure(ResponseMessages.EMPTY_FIELDS));
        } catch (AuthenticationException e) {
            log.warn("Signup failed - user '{}' already exists", request.getUsername());
            clientHandler.send(ResponseFactory.signupFailure(ResponseMessages.USER_EXISTS));
        } catch (Exception e) {
            log.error("Unexpected error in SignupHandler", e);
            throw new MessageProcessingException("SignupHandler failed");
        }
    }

}