package controller.admin;

import service.server.business.admin.AdminService;
import protocol.dto.admin.OnlineUserDTO;

import java.util.List;


public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    public List<OnlineUserDTO> getOnlineUsers() {
        return adminService.getOnlineUsers();
    }

    public void shutdownServer() {
        adminService.shutdownServer();
    }

    public boolean disconnectUser(String username) {
        return adminService.disconnectUser(username);
    }
}