package service.server.core.impl;

import controller.server.router.MessageRouter;
import infrastructure.network.ConnectionManager;
import infrastructure.network.SocketConnectionManager;
import infrastructure.marshalling.JacksonMarshaller;
import infrastructure.marshalling.Marshaller;
import protocol.response.factory.ResponseFactory;
import protocol.response.factory.ResponseFactoryImpl;
import service.server.business.auth.AuthServiceImpl;
import service.server.errorresolver.ServerErrorResolver;
import service.server.session.base.ConnectionRegistry;
import service.server.session.impl.ConnectionRegistryImpl;
import lombok.extern.slf4j.Slf4j;
import service.server.errorresolver.ServerBusinessErrorResolver;
import service.server.errorresolver.ServerSystemErrorResolver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class SocketServer extends AbstractServer {

    private final int port;
    private ServerSocket serverSocket;

    private final ConnectionRegistry connectionRegistry;
    private final ServerErrorResolver systemErrorResolver;

    public SocketServer(int port) {
        this.port = port;
        this.connectionRegistry = new ConnectionRegistryImpl();
        this.systemErrorResolver = ServerSystemErrorResolver.INSTANCE;
    }

    @Override
    protected void acceptClient() {
        AbstractClientConnection clientHandler = null;
        try {
            Socket socket = serverSocket.accept();

            log.info("New client connected: {}", socket.getRemoteSocketAddress());

            ConnectionManager connectionManager = new SocketConnectionManager(socket);

            clientHandler = new SocketClientHandler(connectionManager, connectionRegistry);
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
