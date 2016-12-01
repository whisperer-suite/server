package net.whisperersuite.server.payload.user;

import net.whisperersuite.server.payload.AbstractPayload;

public abstract class AbstractUserPayload extends AbstractPayload {
    private final String username;

    AbstractUserPayload(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
