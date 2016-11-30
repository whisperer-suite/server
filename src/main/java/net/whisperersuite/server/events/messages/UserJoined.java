package net.whisperersuite.server.events.messages;

public class UserJoined extends UserEvent {
    public UserJoined(String username) {
        super(username);
    }

    @Override
    public String eventType() {
        return "USER_JOINED";
    }
}
