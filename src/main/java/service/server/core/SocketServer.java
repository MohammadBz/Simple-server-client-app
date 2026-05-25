package service.server.core;

import infrastructure.network.ConnectionManager;
import infrastructure.network.SocketConnectionManager;
import infrastructure.serialization.JacksonSerializer;
import infrastructure.serialization.Serializer;
import protocol.message.factory.ResponseFactory;
import protocol.message.factory.ResponseFactoryImpl;
import service.server.business.auth.AuthServiceImpl;
import service.server.errorResolver.ServerErrorResolver;
import service.server.session.ConnectionRegistry;
import service.server.session.ConnectionRegistryImpl;
import controller.server.handler.HandlerFactory;
import lombok.extern.slf4j.Slf4j;
import service.server.errorResolver.ServerBusinessErrorResolver;
import service.server.errorResolver.ServerSystemErrorResolver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class SocketServer extends AbstractServer {

    private final int port;
    private ServerSocket serverSocket;

    private final AuthServiceImpl authService;
    private final HandlerFactory handlerFactory;
    private final ConnectionManager connectionManager;
    private final CoreServerManager serverManager;
    private final ConnectionRegistry connectionRegistry;
    private final ServerErrorResolver systemErrorResolver;
    private final ServerErrorResolver serverBusinessErrorResolver;
    private final Serializer serializer;
    private final ResponseFactory responseFactory;

    public SocketServer(int port, CoreServerManager serverManager) {
        this.port = port;
        this.authService = new AuthServiceImpl();
        this.connectionManager = new SocketConnectionManager();
        this.serverManager = serverManager;
        this.serializer = new JacksonSerializer();
        this.responseFactory = new ResponseFactoryImpl(serializer);
        this.handlerFactory = new HandlerFactory(authService, serverManager, serializer, responseFactory);
        this.connectionRegistry = new ConnectionRegistryImpl();
        this.systemErrorResolver = new ServerSystemErrorResolver(responseFactory);
        this.serverBusinessErrorResolver = new ServerBusinessErrorResolver(responseFactory);

    }

    @Override
    protected void acceptClient() {
        ClientHandler clientHandler = null;
        try {
            Socket socket = serverSocket.accept();

            log.info("New client connected: {}", socket.getRemoteSocketAddress());

            ConnectionManager connectionManager = new SocketConnectionManager(socket);

            clientHandler = new ClientHandler(connectionManager, handlerFactory, serverManager, connectionRegistry, serverBusinessErrorResolver, systemErrorResolver, serializer, responseFactory);
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