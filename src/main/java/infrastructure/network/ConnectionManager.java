package infrastructure.network;

import exception.technical.ConnectionException;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

@Slf4j
public class ConnectionManager {

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public ConnectionManager(Socket socket) throws ConnectionException {
        try {
            this.socket = socket;

            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            this.writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            log.info("Connection initialized for {}", socket.getRemoteSocketAddress());

        } catch (IOException e) {
            throw new ConnectionException("Failed to initialize connection");
        }
    }

    public ConnectionManager() {
        socket = null;
        reader = null;
        writer = null;
    }

    public void connect(String host, int port) throws ConnectionException {
        try {
            this.socket = new Socket(host, port);

            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            this.writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true // auto flush
            );

            log.info("Connected to server {}:{}", host, port);

        } catch (IOException e) {
            log.error("Failed to connect to server", e);
            throw new ConnectionException("Could not connect to server");
        }
    }


    public void send(String json) throws ConnectionException {
        try {
            writer.println(json);
            log.debug("Sent message: {}", json);

        } catch (Exception e) {
            log.error("Failed to send message", e);
            throw new ConnectionException("Failed to send message");
        }
    }

    public String receive() throws ConnectionException {
        try {
            String json = reader.readLine();

            if (json == null) {
                throw new ConnectionException("Connection closed by remote host");
            }

            log.debug("Received message: {}", json);
            return json;
        } catch (SocketException e) {
            log.info("Socket closed (normal shutdown)");
            throw new ConnectionException("CONNECTION_CLOSED");
        } catch (IOException e) {
            log.error("I/O error while receiving message", e);
            throw new ConnectionException("RECEIVE_FAILED", e);
        }
    }

    public void close() {
        try {
            if (socket == null) return;
            socket.close();
            log.info("Connection closed");
        } catch (IOException e) {
            log.warn("Error while closing connection", e);
        }
    }
}