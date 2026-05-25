package infrastructure.network;

public interface ConnectionManager {
    void connect(String host, int port);

    void send(String data);

    String receive();

    void close();
}
