package service.client;

import infrastructure.network.ConnectionManager;
import infrastructure.network.SocketConnectionManager;
import launcher.client.event.ClientEventHandler;

public abstract class AbstractClient implements ChatClient {
    protected final ConnectionManager connectionManager;
    protected ClientEventHandler eventHandler;
    protected ClientListener listener;

    public AbstractClient(ClientEventHandler eventHandler) {
        this.connectionManager = new SocketConnectionManager();
        this.eventHandler = eventHandler;
    }

    @Override
    public final void connect(String host, int port) {
        onBeforeConnect(host, port);
        connectionManager.connect(host, port);
        startListener();
        onAfterConnect();
    }

    private void startListener() {
        listener = new ClientListener(connectionManager, eventHandler);
        new Thread(listener).start();
    }

    protected void onBeforeConnect(String host, int port) {
    }

    protected void onAfterConnect() {
    }

    public void setEventHandler(ClientEventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

}
