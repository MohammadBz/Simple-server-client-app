package service.client.impl;

import infrastructure.network.ConnectionManager;
import infrastructure.network.SocketConnectionManager;
import launcher.client.event.ClientEventHandler;
import service.client.base.ChatClient;
import service.client.base.MessageListener;

public abstract class AbstractClient implements ChatClient {
    protected final ConnectionManager connectionManager;
    protected ClientEventHandler eventHandler;
    protected ResponseDispatcher responseDispatcher;
    protected MessageListener listener;

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
        responseDispatcher = new ResponseDispatcher( eventHandler);
        listener = new SocketMessageListener(connectionManager, responseDispatcher, eventHandler);
        listener.start();
    }

    protected void onBeforeConnect(String host, int port) {
    }

    protected void onAfterConnect() {
    }

    public void setEventHandler(ClientEventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

}
