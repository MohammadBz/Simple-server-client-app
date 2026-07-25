package service.client.impl;

import lombok.extern.slf4j.Slf4j;
import service.client.base.MessageListener;

@Slf4j
public abstract class AbstractMessageListener implements MessageListener, Runnable {
    protected volatile boolean running = true;
    private Thread listenerThread;

    @Override
    public void start() {
        running = true;
        listenerThread = new Thread(this);
        listenerThread.start();
    }

    @Override
    public void stop() {
        running = false;
        if (listenerThread != null) {
            listenerThread.interrupt();
        }
    }

    @Override
    public void run() {
        onStart();
        try {
            while (running && !Thread.currentThread().isInterrupted()) {
                listenAndProcess();
            }
        } catch (Exception e) {
            handleError(e);
        } finally {
            onStop();
        }
    }

    protected abstract void listenAndProcess();

    protected abstract void handleError(Exception e);

    protected void onStart() {
        log.info("Listener starting...");
    }

    protected void onStop() {
        log.info("Listener stopped.");
    }
}