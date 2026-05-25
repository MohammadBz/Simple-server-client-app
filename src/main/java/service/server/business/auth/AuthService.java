package service.server.business.auth;

public interface AuthService {
    public void signup(String username, String password);

    public void login(String username, String password);

    public boolean userExists(String username);
}
