package controller.server.handler;

import exception.base.BusinessException;
import protocol.request.BaseRequest;
import service.server.core.ClientConnection;

public interface RequestHandler<T extends BaseRequest> {
    Class<T> requestType();

    void handle(T request, ClientConnection clientConnection) throws BusinessException;
}
