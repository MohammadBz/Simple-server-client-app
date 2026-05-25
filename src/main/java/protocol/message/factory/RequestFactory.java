package protocol.message.factory;

import protocol.message.Message;

public interface RequestFactory {
    Message login(String username, String password);

    Message signup(String username, String password);

    Message sendMessage(String sender, String receiver, String content);

    Message onlineUsersRequest(String requester);

    Message logoutRequest(String username);

    Message disconnectRequest();
}
