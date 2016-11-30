package net.whisperersuite.server.listener.websocket;

import net.whisperersuite.server.events.connection.ConnectionClosedEvent;
import net.whisperersuite.server.events.messages.UserLeft;
import net.whisperersuite.server.messages.MessageService;
import net.whisperersuite.server.websocket.WebSocketSessionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;

@Component
public class NotifyOtherUsersThatUserHasLeftListener {
    private final WebSocketSessionRegistry registry;
    private final MessageService messageService;

    @Autowired
    public NotifyOtherUsersThatUserHasLeftListener(WebSocketSessionRegistry registry, MessageService messageService) {
        this.registry = registry;
        this.messageService = messageService;
    }

    @EventListener
    public void handleEvent(ConnectionClosedEvent event) throws IOException {
        String username = event.getSession().getPrincipal().getName();

        Map<String, WebSocketSession> userSessions = registry.getByUsername(username);
        if (userSessions != null && !userSessions.isEmpty()) {
            return;
        }

        messageService.sendToAllUsersExcept(username, new UserLeft(username));
    }
}
