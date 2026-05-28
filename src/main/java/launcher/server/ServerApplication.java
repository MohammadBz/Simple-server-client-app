package launcher.server;

import controller.admin.AdminController;
import service.server.core.AbstractServer;
import service.server.core.SocketServer;
import lombok.extern.slf4j.Slf4j;
import ui.admin.AdminConsole;
import controller.admin.StandardAdminController;
import service.server.business.admin.AdminServiceImpl;
import service.server.core.CoreServerManager;

@Slf4j
public class ServerApplication {
    private final AbstractServer server;
    private final AdminConsole adminConsole;

    public ServerApplication(int port) {
        log.info("Initializing Server Application on port {}", port);

        CoreServerManager serverManager = new CoreServerManager();

        this.server = new SocketServer(port, serverManager);

        serverManager.setShutdownCapable(server);

        AdminServiceImpl adminService = new AdminServiceImpl(serverManager);
        AdminController controller = new StandardAdminController(adminService);
        this.adminConsole = new AdminConsole(controller);
    }

    public void start() {
        try {
            log.info("Starting background services...");

            Thread consoleThread = new Thread(adminConsole);
            consoleThread.start();

            server.start();

        } catch (Exception e) {
            shutdown();
        }
    }

    public void shutdown() {
        log.info("Initiating application shutdown...");
        server.stop();
    }
}
