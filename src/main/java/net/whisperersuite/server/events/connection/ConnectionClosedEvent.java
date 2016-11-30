package net.whisperersuite.server.events.connection;

import net.whisperersuite.server.events.WebSocketEvent;
import org.springframework.web.socket.WebSocketSession;

public class ConnectionClosedEvent extends WebSocketEvent {
    public ConnectionClosedEvent(WebSocketSession session) {
        super(session);
    }
}
