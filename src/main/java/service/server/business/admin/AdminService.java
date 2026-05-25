package service.server.business.admin;

import protocol.dto.admin.OnlineUserDTO;

import java.util.List;

public interface AdminService {
    List<OnlineUserDTO> getOnlineUsers();

    boolean disconnectUser(String username);

    void shutdownServer();
}