package net.whisperersuite.server.event.connection;

import net.whisperersuite.server.event.WebSocketEvent;
import org.springframework.web.socket.WebSocketSession;

public class ConnectionEstablishedEvent extends WebSocketEvent {
    public ConnectionEstablishedEvent(WebSocketSession session) {
        super(session);
    }
}
