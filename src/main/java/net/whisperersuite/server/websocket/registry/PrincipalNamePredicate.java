package net.whisperersuite.server.websocket.registry;

import org.springframework.web.socket.WebSocketSession;

public class PrincipalNamePredicate implements Predicate<WebSocketSession> {
    private final String principalName;

    PrincipalNamePredicate(String principalName) {
        this.principalName = principalName;
    }

    @Override
    public boolean test(WebSocketSession session) {
        return session.getPrincipal() != null && session.getPrincipal().getName().equals(principalName);
    }
}
