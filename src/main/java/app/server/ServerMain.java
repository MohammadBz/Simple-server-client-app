package app.server;

import service.core.Server;
import lombok.extern.slf4j.Slf4j;
import presentation.admin.AdminConsole;
import presentation.admin.AdminController;
import service.admin.AdminService;
import service.core.ServerManager;

@Slf4j
public class ServerMain {
    public static void main(String[] args) {
        ServerManager serverManager = new ServerManager();

        Server server = new Server(12500, serverManager);
        serverManager.setShutdownCapable(server);
        AdminService adminService = new AdminService(serverManager);
        AdminController controller = new AdminController(adminService);
        AdminConsole console = new AdminConsole(controller);
        try {
            new Thread(console).start();
            server.start();
        } catch (Exception e) {
            log.error("bruh");
        }
    }
}