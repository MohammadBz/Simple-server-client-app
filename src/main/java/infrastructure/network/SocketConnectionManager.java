package infrastructure.network;

import exception.technical.ConnectionException;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

@Slf4j
public class SocketConnectionManager implements ConnectionManager {

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public SocketConnectionManager(Socket socket) throws ConnectionException {
        this.socket = socket;
        initializeStreams();
    }

    public SocketConnectionManager() {
    }

    @Override
    public void connect(String host, int port) throws ConnectionException {
        try {
            this.socket = new Socket(host, port);
            initializeStreams();
            log.info("Successfully connected to {}:{}", host, port);
        } catch (IOException e) {
            throw new ConnectionException("Could not connect to " + host + ":" + port, e);
        }
    }

    private void initializeStreams() throws ConnectionException {
        try {
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        } catch (IOException e) {
            throw new ConnectionException("Failed to initialize I/O streams", e);
        }
    }

    @Override
    public void send(String json) throws ConnectionException {
        if (writer == null) throw new ConnectionException("Not connected");
        writer.println(json);
    }

    @Override
    public String receive() throws ConnectionException {
        try {
            String json = reader.readLine();
            if (json == null) throw new ConnectionException("CONNECTION_CLOSED");
            return json;
        } catch (IOException e) {
            throw new ConnectionException("RECEIVE_FAILED", e);
        }
    }

    @Override
    public void close() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            log.warn("Error closing socket: {}", e.getMessage());
        }
    }
}