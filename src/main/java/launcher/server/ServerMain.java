package launcher.server;

import launcher.server.ServerApplication;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerMain {
    public static void main(String[] args) {
        int port = 12500;

        ServerApplication app = new ServerApplication(port);
        app.start();

    }
}