package application.session;

public class Session {

    private String username;
    private boolean authenticated;

    public Session() {
        this.authenticated = false;
    }

    public void authenticate(String username) {
        this.username = username;
        this.authenticated = true;
    }

    public void clear() {
        this.username = null;
        this.authenticated = false;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public String getUsername() {
        return username;
    }
}