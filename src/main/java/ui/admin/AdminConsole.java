package ui.admin;

import controller.admin.AdminCommand;
import controller.admin.AdminController;
import lombok.extern.slf4j.Slf4j;
import protocol.dto.admin.OnlineUserDTO;

import java.util.List;
import java.util.Scanner;

@Slf4j
public class AdminConsole implements Runnable {

    private final AdminController controller;
    private final Scanner scanner = new Scanner(System.in);

    public AdminConsole(AdminController controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        boolean running = true;

        while (running) {
            printMenu();

            try {
                AdminCommand command = AdminCommand.fromInput(scanner.nextLine());

                switch (command) {
                    case LIST_USERS -> listUsers();
                    case DISCONNECT_USER -> disconnectUser();
                    case SHUTDOWN -> {
                        controller.shutdownServer();
                        running = false;
                    }
                    case EXIT -> running = false;
                }

            } catch (Exception e) {
                System.out.println("Invalid command.");
            }
        }
        log.info("Admin console stopped.");
    }

    private void printMenu() {
        System.out.println("""
                ===== ADMIN PANEL =====
                1. List Users
                2. Disconnect User
                3. Shutdown Server
                4. Exit
                """);
    }

    private void listUsers() {
        List<OnlineUserDTO> users = controller.getOnlineUsers();

        if (users.isEmpty()) {
            System.out.println("No online users.");
            return;
        }

        users.forEach(user -> System.out.println(user.getUsername() + " [" + user.getStatus() + "]"));
    }

    private void disconnectUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        boolean result = controller.disconnectUser(username);

        if (result) {
            System.out.println("User disconnected.");
        } else {
            System.out.println("User not found.");
        }
    }
}