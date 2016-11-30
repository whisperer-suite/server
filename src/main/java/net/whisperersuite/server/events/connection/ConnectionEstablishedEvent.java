package net.whisperersuite.server.events.connection;

import net.whisperersuite.server.events.WebSocketEvent;
import org.springframework.web.socket.WebSocketSession;

public class ConnectionEstablishedEvent extends WebSocketEvent {
    public ConnectionEstablishedEvent(WebSocketSession session) {
        super(session);
    }
}
