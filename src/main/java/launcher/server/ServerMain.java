package launcher.server;

import service.server.core.AbstractServer;
import service.server.core.SocketServer;
import lombok.extern.slf4j.Slf4j;
import ui.admin.AdminConsole;
import controller.admin.AdminController;
import service.server.business.admin.AdminServiceImpl;
import service.server.core.CoreServerManager;

@Slf4j
public class ServerMain {
    public static void main(String[] args) {
        CoreServerManager serverManager = new CoreServerManager();

        AbstractServer server = new SocketServer(12500, serverManager);
        serverManager.setShutdownCapable(server);
        AdminServiceImpl adminService = new AdminServiceImpl(serverManager);
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