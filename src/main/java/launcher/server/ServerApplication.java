package launcher.server;

import service.server.core.impl.AbstractServer;
import service.server.core.impl.SocketServer;
import lombok.extern.slf4j.Slf4j;
import ui.admin.AdminConsole;
import service.server.core.impl.CoreServerManager;

@Slf4j
public class ServerApplication {
    private final AbstractServer server;

    public ServerApplication(int port) {
        log.info("Initializing Server Application on port {}", port);

        CoreServerManager serverManager = CoreServerManager.INSTANCE;

        this.server = new SocketServer(port, CoreServerManager.INSTANCE);

        serverManager.setShutdownCapable(server);
    }

    public void start() {
        try {
            log.info("Starting background services...");

            Thread consoleThread = new Thread(AdminConsole.INSTANCE);
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
