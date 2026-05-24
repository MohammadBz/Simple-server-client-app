package service.server.business.admin;

import java.util.List;

public interface AdminOperations {
    List<String> getOnlineUsers();

    void shutdownServer();

    boolean disconnectUser(String username);
}