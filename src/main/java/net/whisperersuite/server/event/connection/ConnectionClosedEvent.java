package net.whisperersuite.server.event.connection;

import net.whisperersuite.server.event.WebSocketEvent;
import org.springframework.web.socket.WebSocketSession;

public class ConnectionClosedEvent extends WebSocketEvent {
    public ConnectionClosedEvent(WebSocketSession session) {
        super(session);
    }
}
