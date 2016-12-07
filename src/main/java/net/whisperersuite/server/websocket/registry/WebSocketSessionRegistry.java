package net.whisperersuite.server.websocket.registry;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component @Scope("singleton")
public class WebSocketSessionRegistry {
    private final Set<WebSocketSession> sessions = new HashSet<>();

    public Collection<WebSocketSession> all() {
        return Collections.unmodifiableSet(sessions);
    }

    public Collection<WebSocketSession> getByPrincipalName(@NotNull String principalName) {
        return filter(new PrincipalNamePredicate(principalName));
    }

    public void register(@NotNull WebSocketSession session) {
        sessions.add(session);
    }

    public boolean unregister(@NotNull WebSocketSession session) {
        return sessions.remove(session);
    }

    private Collection<WebSocketSession> filter(Predicate<WebSocketSession> predicate) {
        Set<WebSocketSession> set = new HashSet<>();
        for (WebSocketSession session : sessions) {
            if (predicate.test(session)) {
                set.add(session);
            }
        }

        return set;
    }

}
