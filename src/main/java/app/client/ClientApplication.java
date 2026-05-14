package app.client;

import service.client.Client;
import service.client.ClientService;
import service.client.ClientSession;
import presentation.client.ClientController;
import presentation.client.ConsoleUI;
import exception.ConnectionException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientApplication {
    private final ClientSession session;
    private final ConsoleUI ui;
    private final Client client;
    private final ClientService service;
    private final ClientController controller;

    public ClientApplication() {
        this.session = new ClientSession();
        this.ui = new ConsoleUI(session);

        this.client = new Client(null);
        this.service = new ClientService(client, session);

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