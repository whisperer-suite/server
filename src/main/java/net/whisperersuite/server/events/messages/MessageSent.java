package net.whisperersuite.server.events.messages;

import org.springframework.web.socket.WebSocketSession;

public class MessageSent {
    private final WebSocketSession session;
    private final Message message;

    public MessageSent(WebSocketSession session, Message message) {
        this.session = session;
        this.message = message;
    }

    public WebSocketSession getSession() {
        return session;
    }

    public Message getMessage() {
        return message;
    }
}
