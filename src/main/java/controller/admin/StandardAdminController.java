package controller.admin;

import lombok.extern.slf4j.Slf4j;
import service.server.business.admin.AdminService;
import protocol.dto.admin.OnlineUserDTO;
import service.server.business.admin.AdminServiceImpl;

import java.util.List;

@Slf4j
public enum StandardAdminController implements AdminController {
    INSTANCE;
    private static final AdminService adminService = AdminServiceImpl.INSTANCE;

    private StandardAdminController() {
    }

    @Override
    public List<OnlineUserDTO> getOnlineUsers() {
        log.debug("Controller: Requesting online user list");
        return adminService.getOnlineUsers();
    }

    @Override
    public void shutdownServer() {
        log.warn("Controller: Shutdown command received");
        adminService.shutdownServer();
    }

    @Override
    public boolean disconnectUser(String username) {
        if (username == null || username.isBlank()) {
            log.warn("Controller: Disconnect attempted with null/empty username");
            return false;
        }
        log.info("Controller: Requesting disconnect for user: {}", username);
        return adminService.disconnectUser(username);
    }
}