package net.whisperersuite.server.listener.connection;

import net.whisperersuite.server.event.connection.ConnectionClosedEvent;
import net.whisperersuite.server.payload.user.UserLeft;
import net.whisperersuite.server.service.PayloadService;
import net.whisperersuite.server.websocket.registry.WebSocketSessionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Collection;

@Component
public class NotifyOtherUsersThatUserHasLeftListener {
    private final WebSocketSessionRegistry registry;
    private final PayloadService payloadService;

    @Autowired
    public NotifyOtherUsersThatUserHasLeftListener(WebSocketSessionRegistry registry, PayloadService payloadService) {
        this.registry = registry;
        this.payloadService = payloadService;
    }

    @EventListener
    public void handleEvent(ConnectionClosedEvent event) throws IOException {
        String username = event.getSession().getPrincipal().getName();

        Collection<WebSocketSession> userSessions = registry.getByPrincipalName(username);
        if (userSessions != null && !userSessions.isEmpty()) {
            return;
        }

        payloadService.sendToAllUsersExcept(username, new UserLeft(username));
    }
}
