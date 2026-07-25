package controller.client;

import service.client.base.ClientService;
import service.client.base.ClientSession;
import launcher.client.event.ClientEventHandler;
import domain.chat.MessageStatus;
import lombok.extern.slf4j.Slf4j;
import protocol.dto.chat.DeliveryStatusDTO;
import protocol.dto.chat.IncomingMessageDTO;
import protocol.dto.chat.OnlineUsersResponseDTO;
import protocol.response.ResponseDTO;
import protocol.response.ResponseMessages;
import ui.client.ConsoleUI;

import static java.lang.System.exit;

@Slf4j
public class SocketClientController implements ClientEventHandler, ClientController {

    private final ClientService clientService;
    private final ClientSession session;
    private final ConsoleUI ui;

    private volatile boolean running = true;
    private volatile boolean waitingForServer = false;

    public SocketClientController(ClientService clientService, ClientSession session, ConsoleUI ui) {
        this.clientService = clientService;
        this.session = session;
        this.ui = ui;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public boolean isWaitingForServer() {
        return waitingForServer;
    }

    public void setWaitingForServer(boolean value) {
        this.waitingForServer = value;
    }

    @Override
    public void shutdown() {
        running = false;
        log.info("Shutting down client");
        exit(0);
    }

    @Override
    public void login(String username, String password) {
        try {
            waitingForServer = true;
            clientService.login(username, password);
        } catch (Exception e) {
            waitingForServer = false;
            ui.showMessage(e.getMessage());
        }
    }

    @Override
    public void signup(String username, String password) {
        try {
            waitingForServer = true;
            clientService.signup(username, password);
        } catch (Exception e) {
            waitingForServer = false;
            ui.showMessage(e.getMessage());
        }
    }

    @Override
    public void sendMessage(String receiver, String content) {
        try {
            waitingForServer = true;
            clientService.sendMessage(receiver, content);
        } catch (Exception e) {
            waitingForServer = false;
            ui.showMessage(e.getMessage());
        }
    }

    @Override
    public void requestOnlineUsers() {
        try {
            waitingForServer = true;
            clientService.requestOnlineUsers();
        } catch (Exception e) {
            waitingForServer = false;
            ui.showMessage(e.getMessage());
        }
    }

    @Override
    public void logout() {
        try {
            waitingForServer = true;
            clientService.logout();
        } catch (Exception e) {
            waitingForServer = false;
            ui.showMessage(e.getMessage());
        }
    }

    @Override
    public void disconnect() {
        try {
            waitingForServer = true;
            clientService.disconnect();
        } catch (Exception e) {
            waitingForServer = false;
            ui.showMessage(e.getMessage());
            shutdown();
        }
    }

    @Override
    public void onLoginResponse(ResponseDTO response) {
        if (response.isSuccess()) {
            clientService.confirmLogin();
            ui.showMessage("✅ LOGIN SUCCESS: " + response.getMessage());
        } else {
            ui.showMessage("❌ LOGIN FAILED: " + response.getMessage());
        }
        waitingForServer = false;
    }

    @Override
    public void onSignupResponse(ResponseDTO response) {
        if (response.isSuccess()) {
            ui.showMessage("✅ SIGNUP SUCCESS: " + response.getMessage());
        } else {
            ui.showMessage("❌ SIGNUP FAILED: " + response.getMessage());
        }
        waitingForServer = false;
    }

    @Override
    public void onIncomingMessage(IncomingMessageDTO message) {
        ui.showIncomingMessage(message.getSender(), message.getContent());
    }

    @Override
    public void onDeliveryStatus(DeliveryStatusDTO status) {
        if (status.getStatus() == MessageStatus.DELIVERED) {
            ui.showMessage("Message delivered successfully.");
        } else {
            ui.showMessage("Delivery failed: " + status.getDetails());
        }
        waitingForServer = false;
    }

    @Override
    public void onOnlineUsers(OnlineUsersResponseDTO users) {
        ui.showOnlineUsers(users.getUsers());
        waitingForServer = false;
    }

    @Override
    public void onConnectionLost(String reason) {
        ui.showMessage("Connection lost: " + reason);
        shutdown();
    }

    @Override
    public void onDisconnectResponse(ResponseDTO response) {
        ui.showMessage("Disconnected.");
        shutdown();
    }

    @Override
    public void onLogoutResponse(ResponseDTO response) {
        if (response.isSuccess()) {
            session.clear();
            ui.showMessage("Logged out successfully.");
        } else {
            ui.showMessage("Logout failed: " + response.getMessage());
        }
        waitingForServer = false;
    }

    @Override
    public void onSystemNotifications(ResponseDTO response) {
        ui.showMessage("[SYSTEM] " + response.getMessage());

        if (response.getMessage().contains(ResponseMessages.DisconnectedByAdmin) || response.getMessage().contains(ResponseMessages.ServerShuttingDown)) {
            shutdown();
        }
    }
}