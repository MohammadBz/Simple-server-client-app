package controller.server.router;

import controller.server.handler.RequestHandler;
import exception.validation.RouterValidationException;
import lombok.extern.slf4j.Slf4j;
import protocol.request.BaseRequest;
import service.server.core.base.ClientConnection;


@Slf4j
public enum RouterValidator {
    INSTANCE;

    private RouterValidator() {
    }

    public void validateRouter(RequestHandler<? extends BaseRequest> handler, BaseRequest request) {
        if (handler == null) {
            throw new RouterValidationException("No handler found for request type " + request.getClass().getSimpleName());
        }
    }

}
