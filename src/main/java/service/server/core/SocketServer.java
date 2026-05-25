package service.server.core;

import service.server.business.auth.AuthService;
import service.server.session.ConnectionRegistry;
import controller.server.handler.HandlerFactory;
import lombok.extern.slf4j.Slf4j;
import infrastructure.network.ConnectionManager;
import service.server.errorResolver.ServerBusinessErrorResolver;
import service.server.errorResolver.ServerSystemErrorResolver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class SocketServer extends AbstractServer {

    private final int port;
    private ServerSocket serverSocket;

    private final AuthService authService;
    private final HandlerFactory handlerFactory;
    private final ConnectionManager connectionManager;
    private final CoreServerManager serverManager;
    private final ConnectionRegistry connectionRegistry;
    private final ServerSystemErrorResolver systemErrorResolver;
    private final ServerBusinessErrorResolver serverBusinessErrorResolver;

    public SocketServer(int port, CoreServerManager serverManager) {
        this.port = port;
        this.authService = new AuthService();
        this.connectionManager = new ConnectionManager();
        this.serverManager = serverManager;
        this.handlerFactory = new HandlerFactory(authService, serverManager);
        this.connectionRegistry = new ConnectionRegistry();
        this.systemErrorResolver = new ServerSystemErrorResolver();
        this.serverBusinessErrorResolver = new ServerBusinessErrorResolver();
    }

    @Override
    protected void acceptClient() {
        ClientHandler clientHandler = null;
        try {
            Socket socket = serverSocket.accept();

            log.info("New client connected: {}", socket.getRemoteSocketAddress());

            ConnectionManager connectionManager = new ConnectionManager(socket);

            clientHandler = new ClientHandler(connectionManager, handlerFactory, serverManager, connectionRegistry, serverBusinessErrorResolver, systemErrorResolver);
            connectionRegistry.register(clientHandler);

            Thread thread = new Thread(clientHandler);
            thread.start();

        } catch (Exception e) {
            systemErrorResolver.resolve(e, clientHandler);
        }
    }

    @Override
    protected void beforeStart() throws IOException {
        serverSocket = new ServerSocket(port);
        log.info("Server started on port {}", port);
    }

    @Override
    public void stop() {
        if (!running) return;
        running = false;
        log.info("Shutting down server...");
        connectionRegistry.disconnectAll();
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            log.error("Error closing server socket", e);
        }
    }

    @Override
    public void shutdown() {
        this.stop();
    }
}