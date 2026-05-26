package controller.admin;

import protocol.dto.admin.OnlineUserDTO;

import java.util.List;

public interface AdminController {
    List<OnlineUserDTO> getOnlineUsers();

    void shutdownServer();

    boolean disconnectUser(String username);
}
