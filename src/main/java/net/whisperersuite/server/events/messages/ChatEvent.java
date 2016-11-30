package net.whisperersuite.server.events.messages;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class ChatEvent {
    @JsonIgnore
    public abstract String eventType();
}
