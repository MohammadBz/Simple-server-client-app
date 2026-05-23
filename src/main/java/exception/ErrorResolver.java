package exception;

import service.core.ClientHandler;

public interface ErrorResolver {
    public void resolve(Exception e, ClientHandler client);
}
