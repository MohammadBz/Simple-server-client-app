package service.server.core.impl;

import lombok.extern.slf4j.Slf4j;
import service.server.core.base.ClientConnection;

import java.util.UUID;

@Slf4j
public abstract class AbstractClientConnection implements ClientConnection, Runnable {
    protected final String clientId = UUID.randomUUID().toString();
    protected volatile boolean running = true;
    private boolean closed = false;

    @Override
    public final void run() {
        onConnect();
        try {
            while (running && !Thread.currentThread().isInterrupted()) {
                handleCycle();
            }
        } catch (Exception e) {
            handleSystemError(e);
        } finally {
            close();
        }
    }

    protected abstract void handleCycle() throws Exception;

    protected abstract void handleSystemError(Exception e);

    protected abstract void onCleanup();

    public synchronized void close() {
        if (closed) return;
        running = false;
        closed = true;
        onCleanup();
    }

    protected void onConnect() {
        log.info("Client connected: {}", clientId);
    }
}
