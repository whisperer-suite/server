package net.whisperersuite.server.events.messages;

public class UserLeft extends UserEvent {
    public UserLeft(String username) {
        super(username);
    }

    @Override
    public String eventType() {
        return "USER_LEFT";
    }
}
