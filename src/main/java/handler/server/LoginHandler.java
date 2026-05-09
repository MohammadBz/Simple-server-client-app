package handler.server;

import protocol.response.ResponseMessages;
import protocol.dto.auth.LoginRequestDTO;
import exception.AuthenticationException;
import exception.MessageProcessingException;
import exception.ValidationException;
import application.auth.AuthService;
import application.core.ClientHandler;
import protocol.message.Message;
import application.core.ServerManager;
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

        try {
            log.debug("Handling LOGIN_REQUEST from {}", message.getSender());

            ValidationUtil.validateCredentials(request.getUsername(), request.getPassword());

            authService.login(request.getUsername(), request.getPassword());

            clientHandler.getSession().authenticate(request.getUsername());
            serverManager.registerSession(request.getUsername(), clientHandler);
            log.info("User '{}' logged in successfully", request.getUsername());

            clientHandler.send(ResponseFactory.loginSuccess());

        } catch (AuthenticationException e) {
            log.warn("Failed login attempt for user '{}' : {}", request.getUsername(), e.getMessage());
            clientHandler.send(ResponseFactory.loginFailure(ResponseMessages.LOGIN_FAILED));

        } catch (ValidationException e) {
            log.warn("Invalid login input from {}: {}", message.getSender(), e.getMessage());
            clientHandler.send(ResponseFactory.loginFailure(ResponseMessages.EMPTY_FIELDS));
        } catch (Exception e) {
            log.error("Unexpected error in LoginHandler", e);
            throw new MessageProcessingException("LoginHandler failed");
        }
    }


}