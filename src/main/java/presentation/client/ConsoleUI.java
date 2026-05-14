package presentation.client;


import service.client.ClientSession;

import exception.ConnectionException;
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
                System.out.println("Connection error: " + e.getMessage());
                break;

            } catch (Exception e) {
                log.error("Unexpected error : {}", e.getMessage());
                System.out.println("Unexpected error occurred");
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
            default -> System.out.println("Invalid option");
        }
    }

    private void chatLoop() throws ConnectionException {
        showChatMenu();
        int choice = readInt2();

        switch (choice) {
            case 1 -> handleSendMessage();
            case 2 -> handleOnlineUsers();
            case 3 -> handleLogout();
            default -> System.out.println("Invalid option");
        }
    }

    private void showChatMenu() {
        System.out.println("1. Send Message");
        System.out.println("2. Online Users");
        System.out.println("3. Logout");
    }

    private void showAuthMenu() {
        System.out.println("1. Login");
        System.out.println("2. Signup");
        System.out.println("3. EXit");
        System.out.print("Choose: ");
    }

    private void handleLogin() throws ConnectionException {
        log.debug("User selected login");
        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        controller.login(username, password);
    }

    private void handleSignup() throws ConnectionException {
        log.debug("User selected signUp");
        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();
        controller.signup(username, password);

    }

    private void handleSendMessage() throws ConnectionException {

        System.out.print("Receiver username: ");
        String receiver = scanner.nextLine();

        System.out.print("Message content: ");
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
            System.out.println("Please enter a valid number.");
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
        System.out.println();
        System.out.println("Message from " + sender + ":");
        System.out.println(content);
    }

    public void showOnlineUsers(List<String> users) {
        if (users.isEmpty()) {
            System.out.println("No one is online");
            return;
        }
        System.out.println("Online Users:");
        for (String user : users) {
            System.out.println("- " + user);
        }
    }
}