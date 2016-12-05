package net.whisperersuite.server.event.payload;

import net.whisperersuite.server.event.WebSocketEvent;
import net.whisperersuite.server.payload.AbstractPayload;
import org.springframework.web.socket.WebSocketSession;

public class PayloadReceivedEvent <P extends AbstractPayload> extends WebSocketEvent {
    private final P payload;

    public PayloadReceivedEvent(WebSocketSession session, P payload) {
        super(session);
        this.payload = payload;
    }

    public P getPayload() {
        return payload;
    }
}
