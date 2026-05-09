package app.client;

public class ClientMain {
    public static void main(String[] args) {
        ClientApplication app = new ClientApplication();
        app.start("localhost", 12500);
    }
}