package net.whisperersuite.server.event.payload;

import net.whisperersuite.server.payload.AbstractPayload;
import org.springframework.web.socket.WebSocketSession;

public class PayloadReceivedEvent <P extends AbstractPayload> {
    private final WebSocketSession session;
    private final P payload;

    public PayloadReceivedEvent(WebSocketSession session, P payload) {
        this.session = session;
        this.payload = payload;
    }

    public WebSocketSession getSession() {
        return session;
    }

    public P getPayload() {
        return payload;
    }
}
