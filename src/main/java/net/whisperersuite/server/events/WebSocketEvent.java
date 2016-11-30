package net.whisperersuite.server.events;

import org.springframework.web.socket.WebSocketSession;

public abstract class WebSocketEvent {
    private final WebSocketSession session;

    protected WebSocketEvent(WebSocketSession session) {
        this.session = session;
    }

    public WebSocketSession getSession() {
        return session;
    }
}
