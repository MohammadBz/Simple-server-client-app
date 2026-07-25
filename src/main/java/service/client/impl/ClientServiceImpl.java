package service.client.impl;


import exception.technical.ConnectionException;
import exception.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import protocol.request.factory.RequestFactory;
import protocol.request.BaseRequest;
import service.client.base.ChatClient;
import service.client.base.ClientService;
import service.client.base.ClientSession;
import service.common.validation.UserValidator;


@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ChatClient client;
    private final ClientSession session;
    private String pendingLoginUsername;
    private final RequestFactory requestFactory;

    public ClientServiceImpl(ChatClient client, ClientSession session, RequestFactory requestFactory) {
        this.client = client;
        this.session = session;
        this.requestFactory = requestFactory;

    }

    @Override
    public void login(String username, String password) throws ValidationException, ConnectionException {

        UserValidator.validateCredentials(username, password);

        BaseRequest message = requestFactory.login(username, password);

        pendingLoginUsername = username;
        log.info("Sending login request for '{}'", username);
        client.send(message);
    }

    @Override
    public void signup(String username, String password) throws ValidationException, ConnectionException {

        UserValidator.validateCredentials(username, password);

        BaseRequest message = requestFactory.signup(username, password);

        log.info("Sending signup request for '{}'", username);
        client.send(message);
    }

    @Override
    public void sendMessage(String receiver, String content) throws ConnectionException {

        log.info("Sending message to '{}'", receiver);

        BaseRequest message = requestFactory.sendMessage(session.getUsername(), receiver, content);

        client.send(message);
    }

    @Override
    public void requestOnlineUsers() throws ConnectionException {

        log.info("Requesting online users");

        BaseRequest request = requestFactory.onlineUsersRequest(session.getUsername());

        client.send(request);
    }

    @Override
    public void disconnect() throws ConnectionException {
        log.info("Sending Disconnect request");
        BaseRequest request = requestFactory.disconnectRequest();
        client.send(request);
    }

    @Override
    public void logout() throws ConnectionException {

        log.info("Sending logout request for user {}", session.getUsername());

        BaseRequest request = requestFactory.logoutRequest(session.getUsername());

        client.send(request);
    }

    @Override
    public void confirmLogin() {
        session.authenticate(pendingLoginUsername);
        log.info("Client session authenticated for '{}'", pendingLoginUsername);
        pendingLoginUsername = null;
    }
}
