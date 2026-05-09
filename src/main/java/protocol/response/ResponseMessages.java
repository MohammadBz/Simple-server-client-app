package protocol.response;

public class ResponseMessages {
    public static final String LOGIN_SUCCESS = "Login successful";
    public static final String LOGIN_FAILED = "Invalid credentials";

    public static final String SIGNUP_SUCCESS = "Signup successful";
    public static final String USER_EXISTS = "User already exists";

    public static final String EMPTY_FIELDS = "Username or password cannot be empty";

    public static final String MESSAGE_SENT = "Message sent successfully";
    public static final String MESSAGE_FAILED = "Message delivery failed";
    public static final String USER_OFFLINE = "Recipient is offline";
    public static final String UNAUTHORIZED = "User not authenticated";
    public static final String INVALID_MESSAGE = "Invalid message content";

    public static final String ONLINE_USERS_FETCHED = "Online users retrieved";

    public static final String DisconnectedByAdmin = "Disconnected by admin";
    public static final String ServerShuttingDown = "Server shutting down";
}
