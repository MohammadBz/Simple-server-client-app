package service.client.impl;

import lombok.extern.slf4j.Slf4j;
import service.client.base.ClientSession;

@Slf4j
public class ClientSessionImpl implements ClientSession {

    private String username;
    private boolean authenticated;

    public void authenticate(String username) {
        this.username = username;
        this.authenticated = true;
    }

    public void clear() {
        log.info("Clearing client session");

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
