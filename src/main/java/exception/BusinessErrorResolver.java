package exception;

import domain.chat.MessageStatus;
import lombok.extern.slf4j.Slf4j;
import protocol.message.factory.ResponseFactory;
import protocol.response.ResponseMessages;
import service.core.ClientHandler;

import java.util.Map;


import java.util.HashMap;

@Slf4j
public class BusinessErrorResolver implements ErrorResolver {
    private final Map<Class<? extends Exception>, ErrorAction> registry = new HashMap<>();

    public BusinessErrorResolver() {
        register(LoginValidationException.class, new LoginValidationExceptionErrorAction());
        register(SignupValidationException.class, new SignupValidationExceptionErrorAction());
        register(InvalidCredentialsException.class, new InvalidCredentialsExceptionErrorAction());
        register(UserDuplicateConflictException.class, new UserDuplicateConflictExceptionErrorAction());
        register(UnauthorizedException.class, new UnauthorizedExceptionErrorAction());
        register(MessageValidationException.class, new MessageValidationExceptionErrorAction());
        register(MessageRoutingException.class, new MessageRoutingExceptionErrorAction());
    }

    @Override
    public void resolve(Exception e, ClientHandler client) {
        ErrorAction action = registry.get(e.getClass());

        if (action != null) {
            action.execute(e, client);
        } else {
            log.error("Unhandled business exception of type {}: {}", e.getClass().getSimpleName(), e.getMessage());
        }
    }

    @FunctionalInterface
    public interface ErrorAction {
        void execute(Exception e, ClientHandler client);
    }

    public class LoginValidationExceptionErrorAction implements ErrorAction {
        @Override
        public void execute(Exception e, ClientHandler client) {
            LoginValidationException ex = (LoginValidationException) e;
            log.warn("Invalid login input from {}: {}", ex.getUsername(), e.getMessage());
            client.send(ResponseFactory.loginFailure(ResponseMessages.EMPTY_FIELDS));
        }
    }

    public class MessageRoutingExceptionErrorAction implements ErrorAction {
        @Override
        public void execute(Exception e, ClientHandler client) {
            sendDeliveryFailure(client, e.getMessage());
            log.warn("Message routing failed: {}", e.getMessage());
        }
    }

    public class MessageValidationExceptionErrorAction implements ErrorAction {
        @Override
        public void execute(Exception e, ClientHandler client) {
            sendDeliveryFailure(client, e.getMessage());
            log.warn("Invalid message: {}", e.getMessage());
        }
    }

    public class SignupValidationExceptionErrorAction implements ErrorAction {
        @Override
        public void execute(Exception e, ClientHandler client) {
            SignupValidationException ex = (SignupValidationException) e;
            log.warn("Invalid signup input from {}: {}", ex.getUsername(), e.getMessage());
            client.send(ResponseFactory.signupFailure(ResponseMessages.EMPTY_FIELDS));
        }
    }

    public class UnauthorizedExceptionErrorAction implements ErrorAction {
        @Override
        public void execute(Exception e, ClientHandler client) {
            client.send(ResponseFactory.deliveryStatus(null, MessageStatus.FAILED, ResponseMessages.UNAUTHORIZED));
            log.warn("Unauthorized action attempted", e);
        }
    }

    public class UserDuplicateConflictExceptionErrorAction implements ErrorAction {
        @Override
        public void execute(Exception e, ClientHandler client) {
            UserDuplicateConflictException ex = (UserDuplicateConflictException) e;
            log.warn("Signup failed - user '{}' already exists", ex.getUsername());
            client.send(ResponseFactory.signupFailure(ResponseMessages.USER_EXISTS));
        }
    }

    public class InvalidCredentialsExceptionErrorAction implements ErrorAction {
        @Override
        public void execute(Exception e, ClientHandler client) {
            InvalidCredentialsException ex = (InvalidCredentialsException) e;
            log.warn("Failed login attempt for user '{}' : {}", ex.getUsername(), e.getMessage());
            client.send(ResponseFactory.loginFailure(ResponseMessages.LOGIN_FAILED));
        }
    }

    private void sendDeliveryFailure(ClientHandler client, String message) {
        client.send(ResponseFactory.deliveryStatus(null, MessageStatus.FAILED, message));
    }

    private void register(Class<? extends Exception> type, ErrorAction action) {
        registry.put(type, action);
    }

}
