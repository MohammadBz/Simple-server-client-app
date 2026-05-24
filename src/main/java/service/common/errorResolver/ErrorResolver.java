package service.common.errorResolver;

import service.server.core.ClientHandler;

public interface ErrorResolver {
    public void resolve(Exception e, ClientHandler client);
}
