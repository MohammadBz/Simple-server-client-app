package controller.client;

public interface ClientController {
    void login(String username, String password);

    void signup(String username, String password);

    void sendMessage(String receiver, String content);

    void requestOnlineUsers();

    void logout();

    void disconnect();

    void shutdown();

    boolean isRunning();

    boolean isWaitingForServer();
}
