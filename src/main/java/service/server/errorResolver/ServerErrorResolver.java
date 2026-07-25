package service.server.errorresolver;

import service.server.core.base.ClientConnection;


public interface ServerErrorResolver {
    public void resolve(Exception e, ClientConnection client);
}
