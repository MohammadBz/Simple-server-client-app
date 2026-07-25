package controller.server.handler;

import exception.business.MessageProcessingException;
import exception.validation.SignupValidationException;
import exception.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import protocol.response.factory.ResponseFactory;
import protocol.request.SignupRequest;
import service.common.validation.UserValidator;
import service.server.business.auth.AuthService;
import service.server.core.base.ClientConnection;

@Slf4j
public class  SignupHandler implements RequestHandler<SignupRequest> {

    private final AuthService authService;
    private final ResponseFactory responseFactory;

    public SignupHandler(AuthService authService, ResponseFactory responseFactory) {
        this.authService = authService;
        this.responseFactory = responseFactory;
    }

    @Override
    public Class<SignupRequest> requestType() {
        return SignupRequest.class;
    }

    @Override
    public void handle(SignupRequest request, ClientConnection clientConnection) throws MessageProcessingException {
        log.debug("Handling SIGNUP_REQUEST from {}", request.getSender());
        try {
            UserValidator.validateCredentials(request.getSender(), request.getPassword());
        } catch (ValidationException e) {
            throw new SignupValidationException(e.getMessage(), request.getSender());
        }

        authService.signup(request.getSender(), request.getPassword());
        clientConnection.getSession().authenticate(request.getSender());

        log.info("User '{}' signed up successfully", request.getSender());
        clientConnection.send(responseFactory.signupSuccess());
    }
}
