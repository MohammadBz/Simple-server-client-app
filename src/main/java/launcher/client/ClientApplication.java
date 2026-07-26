package launcher.client;

import controller.client.SocketClientController;
import infrastructure.marshalling.JacksonMarshaller;
import infrastructure.marshalling.Marshaller;
import protocol.request.factory.RequestFactory;
import protocol.request.factory.RequestFactoryImpl;
import service.client.base.ClientService;
import service.client.base.ClientSession;
import service.client.impl.AbstractClient;
import service.client.impl.ClientServiceImpl;
import service.client.impl.ClientSessionImpl;
import service.client.impl.SocketChatClient;
import ui.client.ConsoleUI;
import exception.technical.ConnectionException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientApplication {
    private final ClientSession session;
    private final ConsoleUI ui;
    private final AbstractClient client;
    private final ClientService service;
    private final SocketClientController controller;
    private final Marshaller<String> marshaller;
    private final RequestFactory requestFactory;

    public ClientApplication() {
        this.session = new ClientSessionImpl();
        this.ui = new ConsoleUI(session);
        this.marshaller = JacksonMarshaller.getInstance();
        this.client = new SocketChatClient(null, marshaller);
        this.requestFactory = RequestFactoryImpl.INSTANCE;
        this.service = new ClientServiceImpl(client, session, requestFactory);

        this.controller = new SocketClientController(service, session, ui);

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
