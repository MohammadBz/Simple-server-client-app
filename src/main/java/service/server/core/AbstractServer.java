package service.server.core;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public abstract class AbstractServer implements Server, ShutdownCapable {
    protected volatile boolean running = true;

    @Override
    public final void start() {
        try {
            beforeStart();
            runServerLoop();
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

    protected abstract void beforeStart() throws IOException;

    protected abstract void acceptClient();

    private void runServerLoop() throws Exception {
        while (running) {
            acceptClient();
        }
    }

}
