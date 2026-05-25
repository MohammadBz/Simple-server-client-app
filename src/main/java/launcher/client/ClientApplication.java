package launcher.client;

import infrastructure.serialization.JacksonSerializer;
import infrastructure.serialization.Serializer;
import protocol.message.factory.RequestFactory;
import protocol.message.factory.RequestFactoryImpl;
import service.client.*;
import controller.client.ClientController;
import ui.client.ConsoleUI;
import exception.technical.ConnectionException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientApplication {
    private final ClientSession session;
    private final ConsoleUI ui;
    private final AbstractClient client;
    private final ClientService service;
    private final ClientController controller;
    private final Serializer serializer;
    private final RequestFactory requestFactory;

    public ClientApplication() {
        this.session = new ClientSessionImpl();
        this.ui = new ConsoleUI(session);
        this.serializer = new JacksonSerializer();
        this.client = new SocketChatClient(null, serializer);
        this.requestFactory = new RequestFactoryImpl(serializer);
        this.service = new ClientServiceImpl(client, session, requestFactory);

        this.controller = new ClientController(service, session, ui);

        this.ui.setController(controller);
        this.client.setEventHandler(controller);


    }

    public void start(String host, int port) {
        try {
            client.connect(host, port);
            ui.start(controller);

        } catch (ConnectionException e) {
            log.error("Failed to connect: {}", e.getMessage());
            ui.showMessage("Unable to connect to server.");
        } finally {
            shutdown();
        }
    }

    public void shutdown() {
        try {
            client.disconnect();
        } catch (Exception e) {
            log.warn("Error during shutdown");
        }
        log.info("Client application stopped.");
    }
}