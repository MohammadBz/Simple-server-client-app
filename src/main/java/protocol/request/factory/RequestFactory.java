package protocol.request.factory;

import protocol.request.BaseRequest;

public interface RequestFactory {
    BaseRequest login(String username, String password);

    BaseRequest signup(String username, String password);

    BaseRequest sendMessage(String sender, String receiver, String content);

    BaseRequest onlineUsersRequest(String requester);

    BaseRequest logoutRequest(String username);

    BaseRequest disconnectRequest();
}
