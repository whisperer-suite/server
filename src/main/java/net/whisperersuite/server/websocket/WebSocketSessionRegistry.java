package net.whisperersuite.server.websocket;

import net.whisperersuite.server.util.TwoLevelIterator;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component @Scope("singleton")
public class WebSocketSessionRegistry {
    private final Map<String, Map<String, WebSocketSession>> sessions = new HashMap<>();

    public Iterator<WebSocketSession> all() {
        return new TwoLevelIterator<>(
            sessions.values().iterator(),
            new TwoLevelIterator.Function<Map<String, WebSocketSession>, WebSocketSession>() {
                @Override
                public Iterator<WebSocketSession> getIterator(Map<String, WebSocketSession> value) {
                    return value.values().iterator();
                }
            }
        );
    }

    public WebSocketSession get(String id) {
        for (Map<String, WebSocketSession> userSessions : sessions.values()) {
            if (userSessions.containsKey(id)) {
                return userSessions.get(id);
            }
        }

        return null;
    }

    public String getUsername(String id) {
        for (Map.Entry<String, Map<String, WebSocketSession>> entry : sessions.entrySet()) {
            if (entry.getValue().containsKey(id)) {
                return entry.getKey();
            }
        }

        return null;
    }

    public Map<String, WebSocketSession> getByUsername(String username) {
        Map<String, WebSocketSession> userSessions = sessions.get(username);

        return userSessions == null ? null : Collections.unmodifiableMap(userSessions);
    }

    public void register(WebSocketSession session, String username) {
        Map<String, WebSocketSession> userSessions;
        synchronized (sessions) {
            userSessions = sessions.get(username);
            if (userSessions == null) {
                userSessions = new HashMap<>();
                sessions.put(username, userSessions);
            }
        }
        userSessions.put(session.getId(), session);
    }

    public boolean unregister(WebSocketSession session) {
        return null == unregister(session.getId());
    }

    public WebSocketSession unregister(String id) {
        for (Map<String, WebSocketSession> userSessions : sessions.values()) {
            if (userSessions.containsKey(id)) {
                return userSessions.remove(id);
            }
        }

        return null;
    }
}
