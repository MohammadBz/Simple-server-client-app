package service.server.errorresolver;

import service.server.core.ClientConnection;


public interface ServerErrorResolver {
    public void resolve(Exception e, ClientConnection client);
}
