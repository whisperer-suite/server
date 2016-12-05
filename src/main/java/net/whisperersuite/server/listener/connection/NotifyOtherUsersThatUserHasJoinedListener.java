package net.whisperersuite.server.listener.connection;

import net.whisperersuite.server.event.connection.ConnectionEstablishedEvent;
import net.whisperersuite.server.payload.user.UserJoined;
import net.whisperersuite.server.service.PayloadService;
import net.whisperersuite.server.websocket.WebSocketSessionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;

@Component
public class NotifyOtherUsersThatUserHasJoinedListener {
    private final WebSocketSessionRegistry registry;
    private final PayloadService payloadService;

    @Autowired
    public NotifyOtherUsersThatUserHasJoinedListener(WebSocketSessionRegistry registry, PayloadService payloadService) {
        this.registry = registry;
        this.payloadService = payloadService;
    }

    @EventListener
    public void handleEvent(ConnectionEstablishedEvent event) throws IOException {
        String username = event.getSession().getPrincipal().getName();

        Map<String, WebSocketSession> userSessions = registry.getByUsername(username);
        if (userSessions != null && userSessions.size() > 1) {
            return;
        }

        payloadService.sendToAllUsersExcept(username, new UserJoined(username));
    }
}
