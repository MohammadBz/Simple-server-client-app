package service.server.business.admin;

import lombok.extern.slf4j.Slf4j;
import protocol.dto.admin.OnlineUserDTO;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class AdminServiceImpl implements AdminService {

    private final AdminOperations adminOperations;

    public AdminServiceImpl(AdminOperations adminOperations) {
        this.adminOperations = adminOperations;
    }

    @Override
    public List<OnlineUserDTO> getOnlineUsers() {
        log.info("Fetching online users for admin");

        return adminOperations.getOnlineUsers().stream().map(username -> new OnlineUserDTO(username, "ONLINE")).collect(Collectors.toList());
        // map List<String> users to a list of Online users DTO
    }

    @Override
    public boolean disconnectUser(String username) {
        log.info("Admin requested disconnect for user: {}", username);

        boolean result = adminOperations.disconnectUser(username);

        if (result) {
            log.info("User {} disconnected successfully", username);
        } else {
            log.warn("Failed to disconnect user {}", username);
        }
        return result;
    }

    @Override
    public void shutdownServer() {
        log.warn("CRITICAL: Server shutdown initiated via AdminService");
        adminOperations.shutdownServer();
    }
}