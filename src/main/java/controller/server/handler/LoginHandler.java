package controller.server.handler;

import exception.business.MessageProcessingException;
import exception.validation.LoginValidationException;
import exception.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import protocol.response.factory.ResponseFactory;
import protocol.request.LoginRequest;
import service.common.validation.UserValidator;
import service.server.business.auth.AuthService;
import service.server.core.base.ClientConnection;
import service.server.core.base.SessionOperations;

@Slf4j
public class LoginHandler implements RequestHandler<LoginRequest> {

    private final AuthService authService;
    private final SessionOperations sessionOperations;
    private final ResponseFactory responseFactory;

    public LoginHandler(AuthService authService, SessionOperations sessionOperations, ResponseFactory responseFactory) {
        this.authService = authService;
        this.sessionOperations = sessionOperations;
        this.responseFactory = responseFactory;
    }

    @Override
    public Class<LoginRequest> requestType() {
        return LoginRequest.class;
    }

    @Override
    public void handle(LoginRequest request, ClientConnection clientConnection) throws MessageProcessingException {
        log.debug("Handling LOGIN_REQUEST from {}", request.getSender());
        validateLogin(request);

        authService.login(request.getSender(), request.getPassword());
        clientConnection.getSession().authenticate(request.getSender());
        sessionOperations.registerSession(request.getSender(), clientConnection);

        log.info("User '{}' logged in successfully", request.getSender());
        clientConnection.send(responseFactory.loginSuccess());
    }

    private void validateLogin(LoginRequest request) {
        try {
            UserValidator.validateCredentials(request.getSender(), request.getPassword());
        } catch (ValidationException e) {
            throw new LoginValidationException(request.getSender(), e.getMessage());
        }
    }
}
