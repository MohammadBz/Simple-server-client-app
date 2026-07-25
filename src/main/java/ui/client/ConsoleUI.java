package ui.client;


import controller.client.ClientController;
import service.client.base.ClientSession;

import exception.technical.ConnectionException;
import lombok.extern.slf4j.Slf4j;


import java.util.List;
import java.util.Scanner;

@Slf4j
public class ConsoleUI {
    private final ClientSession session;
    private ClientController controller;
    private final Scanner scanner = new Scanner(System.in);


    public ConsoleUI(ClientSession session) {

        this.session = session;
    }

    public void setController(ClientController controller) {
        this.controller = controller;
    }


    public void start(ClientController controller) {
        while (controller.isRunning()) {
            try {

                if (controller.isWaitingForServer()) {
                    Thread.sleep(200);
                    continue;
                }

                if (!session.isAuthenticated()) {
                    authLoop();
                } else {
                    chatLoop();
                }

            } catch (ConnectionException e) {
                log.error("Connection Error : {}", e.getMessage());
                showMessage("Connection error: " + e.getMessage());
                break;

            } catch (Exception e) {
                log.error("Unexpected error : {}", e.getMessage());
                showMessage("Unexpected error");
            }
        }
    }

    private void authLoop() throws ConnectionException {
        showAuthMenu();
        int choice = readInt2();

        switch (choice) {
            case 1 -> handleLogin();
            case 2 -> handleSignup();
            case 3 -> handleExit();
            default -> showMessage("Invalid option");
        }
    }

    private void chatLoop() throws ConnectionException {
        showChatMenu();
        int choice = readInt2();

        switch (choice) {
            case 1 -> handleSendMessage();
            case 2 -> handleOnlineUsers();
            case 3 -> handleLogout();
            default -> showMessage("Invalid option");
        }
    }

    private void showChatMenu() {
        showMessage("1. Send Message");
        showMessage("2. Online Users");
        showMessage("3. Logout");
    }

    private void showAuthMenu() {
        showMessage("1. Login");
        showMessage("2. Signup");
        showMessage("3. Exit");
        showMessage("Choose :");
    }

    private void handleLogin() throws ConnectionException {
        log.debug("User selected login");
        showMessage("Username: ");
        String username = scanner.nextLine();

        showMessage("Password: ");
        String password = scanner.nextLine();

        controller.login(username, password);
    }

    private void handleSignup() throws ConnectionException {
        log.debug("User selected signUp");
        showMessage("Username: ");
        String username = scanner.nextLine();

        showMessage("Password: ");
        String password = scanner.nextLine();
        controller.signup(username, password);

    }

    private void handleSendMessage() throws ConnectionException {

        showMessage("Receiver username: ");
        String receiver = scanner.nextLine();

        showMessage("Message content: ");
        String content = scanner.nextLine();

        controller.sendMessage(receiver, content);
    }

    private void handleOnlineUsers() throws ConnectionException {
        controller.requestOnlineUsers();
    }

    private void handleLogout() {
        controller.logout();
    }

    private void handleExit() throws ConnectionException {
        controller.disconnect();
    }

    private int readInt2() {
        while (!scanner.hasNextInt()) {
            showMessage("Please enter a valid number.");
            scanner.nextLine();
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

    public void showMessage(String text) {
        System.out.println(text);
    }

    public void showIncomingMessage(String sender, String content) {
        showMessage("");
        showMessage("Message from " + sender + ":");
        showMessage(content);
    }

    public void showOnlineUsers(List<String> users) {
        if (users.isEmpty()) {
            showMessage("There are no online users");
            return;
        }
        showMessage("Online Users:");
        for (String user : users) {
            showMessage(" - " + user);
        }
    }
}