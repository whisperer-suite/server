package net.whisperersuite.server.events.messages;

public abstract class UserEvent extends ChatEvent {
    private final String username;

    UserEvent(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
