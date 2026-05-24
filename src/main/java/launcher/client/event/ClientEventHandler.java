package launcher.client.event;

import protocol.dto.chat.DeliveryStatusDTO;
import protocol.dto.chat.IncomingMessageDTO;
import protocol.dto.chat.OnlineUsersResponseDTO;
import protocol.response.ResponseDTO;

public interface ClientEventHandler {

    void onLoginResponse(ResponseDTO response);

    void onSignupResponse(ResponseDTO response);

    void onIncomingMessage(IncomingMessageDTO message);

    void onDeliveryStatus(DeliveryStatusDTO status);

    void onOnlineUsers(OnlineUsersResponseDTO users);

    void onConnectionLost(String reason);

    void onDisconnectResponse(ResponseDTO response);

    void onLogoutResponse(ResponseDTO response);

    void onSystemNotifications(ResponseDTO response);
}