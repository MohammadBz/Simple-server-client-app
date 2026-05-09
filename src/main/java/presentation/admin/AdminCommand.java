package presentation.admin;

public enum AdminCommand {
    LIST_USERS,
    DISCONNECT_USER,
    EXIT,
    SHUTDOWN;

    public static AdminCommand fromInput(String input) {
        return switch (input.trim().toLowerCase()) {
            case "1", "list" -> LIST_USERS;
            case "2", "disconnect" -> DISCONNECT_USER;
            case "3", "shutdown" -> SHUTDOWN;
            case "4", "exit" -> EXIT;
            default -> throw new IllegalArgumentException("Invalid command");
        };
    }
}