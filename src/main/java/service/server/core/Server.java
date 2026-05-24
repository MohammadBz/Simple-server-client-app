package service.server.core;

import service.server.business.auth.AuthService;
import service.server.session.ConnectionRegistry;
import exception.technical.ConnectionException;
import controller.server.handler.HandlerFactory;
import lombok.extern.slf4j.Slf4j;
import infrastructure.network.ConnectionManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

@Slf4j
public class Server implements ShutdownCapable {

    private final int port;
    private ServerSocket serverSocket;
    private volatile boolean running = true;

    private final AuthService authService;
    private final HandlerFactory handlerFactory;
    private final ConnectionManager connectionManager;
    private final ServerManager serverManager;
    private final ConnectionRegistry connectionRegistry;

    public Server(int port, ServerManager serverManager) {
        this.port = port;
        this.authService = new AuthService();
        this.connectionManager = new ConnectionManager();
        this.serverManager = serverManager;
        this.handlerFactory = new HandlerFactory(authService, serverManager);
        this.connectionRegistry = new ConnectionRegistry();
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            log.info("Server started on port {}", port);

            while (running) {
                acceptClient();
            }

        } catch (IOException e) {
            if (running) {
                log.error("Server crashed", e);
            }
        } catch (Exception e) {
            if (running) {
                log.error("Unexpected error", e);
            }
        } finally {
            stop();
        }
    }

    private void acceptClient() {
        try {
            Socket socket = serverSocket.accept();

            log.info("New client connected: {}", socket.getRemoteSocketAddress());

            ConnectionManager connectionManager = new ConnectionManager(socket);

            ClientHandler clientHandler = new ClientHandler(connectionManager, handlerFactory, serverManager, connectionRegistry);
            connectionRegistry.register(clientHandler);

            Thread thread = new Thread(clientHandler);
            thread.start();

        } catch (SocketException e) {
            log.warn("Socket closed", e);
        } catch (ConnectionException e) {
            log.warn("Failed to initialize client connection: {}", e.getMessage());
        } catch (IOException e) {
            log.error("Error accepting client connection", e);
        } catch (Exception e) {
            log.error("Unknown Error", e);
        }
    }

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