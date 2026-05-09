package application.admin;

import lombok.extern.slf4j.Slf4j;
import protocol.dto.admin.OnlineUserDTO;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class AdminService {

    private final AdminOperations adminOperations;

    public AdminService(AdminOperations adminOperations) {
        this.adminOperations = adminOperations;
    }


    public List<OnlineUserDTO> getOnlineUsers() {
        log.info("Fetching online users for admin");

        return adminOperations.getOnlineUsers().stream().map(username -> new OnlineUserDTO(username, "ONLINE")).collect(Collectors.toList());
        // map List<String> users to a list of Online users DTO
    }

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

    public void shutdownServer() {
        adminOperations.shutdownServer();
    }
}