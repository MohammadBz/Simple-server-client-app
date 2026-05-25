package service.client;

import exception.technical.ConnectionException;
import exception.validation.ValidationException;

public interface ClientService {
    void login(String username, String password) throws ValidationException, ConnectionException;

    void signup(String username, String password) throws ValidationException, ConnectionException;

    void sendMessage(String receiver, String content) throws ConnectionException;

    void requestOnlineUsers() throws ConnectionException;

    void logout() throws ConnectionException;

    void disconnect() throws ConnectionException;

    void confirmLogin();
}
