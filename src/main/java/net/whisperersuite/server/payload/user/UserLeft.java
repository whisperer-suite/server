package net.whisperersuite.server.payload.user;

public class UserLeft extends AbstractUserPayload {
    public UserLeft(String username) {
        super(username);
    }

    @Override
    public String getType() {
        return "USER_LEFT";
    }
}
