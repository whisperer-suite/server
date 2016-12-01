package net.whisperersuite.server.payload.user;

public class UserJoined extends AbstractUserPayload {
    public UserJoined(String username) {
        super(username);
    }

    @Override
    public String getType() {
        return "USER_JOINED";
    }
}
