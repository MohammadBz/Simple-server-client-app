package service.common.errorResolver;

import service.server.core.ClientConnection;


public interface ErrorResolver {
    public void resolve(Exception e, ClientConnection client);
}
