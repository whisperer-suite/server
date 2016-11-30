package net.whisperersuite.server.listener.websocket;

import net.whisperersuite.server.events.connection.ConnectionEstablishedEvent;
import net.whisperersuite.server.events.messages.UserJoined;
import net.whisperersuite.server.messages.MessageService;
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
    private final MessageService messageService;

    @Autowired
    public NotifyOtherUsersThatUserHasJoinedListener(WebSocketSessionRegistry registry, MessageService messageService) {
        this.registry = registry;
        this.messageService = messageService;
    }

    @EventListener
    public void handleEvent(ConnectionEstablishedEvent event) throws IOException {
        String username = event.getSession().getPrincipal().getName();

        Map<String, WebSocketSession> userSessions = registry.getByUsername(username);
        if (userSessions == null || userSessions.size() > 1) {
            return;
        }

        messageService.sendToAllUsersExcept(username, new UserJoined(username));
    }
}
