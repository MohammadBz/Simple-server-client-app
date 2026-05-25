package service.client;

public interface ClientSession {
    public void authenticate(String username);

    public void clear();

    public boolean isAuthenticated();

    public String getUsername();
}
