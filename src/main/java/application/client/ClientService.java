package application.client;

import protocol.dto.auth.LoginRequestDTO;
import protocol.dto.auth.SignupRequestDTO;
import exception.ConnectionException;
import exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import protocol.message.Message;
import protocol.message.MessageType;
import validation.ValidationUtil;
import infrastructure.serialization.JsonUtil;
import protocol.message.factory.RequestFactory;


@Slf4j
public class ClientService {

    private final Client client;
    private final ClientSession session;
    private String pendingLoginUsername;

    public ClientService(Client client, ClientSession session) {
        this.client = client;
        this.session = session;

    }

    public void login(String username, String password) throws ValidationException, ConnectionException {

        ValidationUtil.validateCredentials(username, password);

        LoginRequestDTO dto = new LoginRequestDTO(username, password);

        Message message = new Message(MessageType.LOGIN_REQUEST, username, JsonUtil.toJson(dto));
        pendingLoginUsername = username;
        log.info("Sending login request for '{}'", username);
        client.send(message);
    }

    public void signup(String username, String password) throws ValidationException, ConnectionException {

        ValidationUtil.validateCredentials(username, password);

        SignupRequestDTO dto = new SignupRequestDTO(username, password);

        Message message = new Message(MessageType.SIGNUP_REQUEST, username, JsonUtil.toJson(dto));
        log.info("Sending signup request for '{}'", username);
        client.send(message);
    }

    public void sendMessage(String receiver, String content) throws ConnectionException {

        log.info("Sending message to '{}'", receiver);

        Message message = RequestFactory.sendMessage(session.getUsername(), receiver, content);

        client.send(message);
    }

    public void requestOnlineUsers() throws ConnectionException {

        log.info("Requesting online users");

        Message request = RequestFactory.onlineUsersRequest(session.getUsername());

        client.send(request);
    }

    public void disconnect() throws ConnectionException {
        log.info("Sending Disconnect request");
        Message request = RequestFactory.disconnectRequest();
        client.send(request);
    }

    public void logout() throws ConnectionException {

        log.info("Sending logout request for user {}", session.getUsername());

        Message request = RequestFactory.logoutRequest(session.getUsername());

        client.send(request);
    }

    public void confirmLogin() {
        session.authenticate(pendingLoginUsername);
        log.info("Client session authenticated for '{}'", pendingLoginUsername);
    }
}