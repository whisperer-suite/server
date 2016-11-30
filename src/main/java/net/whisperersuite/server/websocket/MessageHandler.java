package net.whisperersuite.server.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

@Component
public class MessageHandler extends TextWebSocketHandler {
    private static final Logger log = LoggerFactory.getLogger(MessageHandler.class);

    private final WebSocketSessionRegistry registry;

    @Autowired
    public MessageHandler(WebSocketSessionRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("User {} connected", session.getPrincipal().getName());

        Map map = registry.getByUsername(session.getPrincipal().getName());
        boolean justArrived = map == null || map.isEmpty();

        registry.register(session, session.getPrincipal().getName());
        if (justArrived) {
            sendToOtherUsers(session, new TextMessage(String.format("User '%s' joined", session.getPrincipal().getName())));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("User {} disconnected: {}", session.getPrincipal().getName(), status.getReason());
        registry.unregister(session);

        Map map = registry.getByUsername(session.getPrincipal().getName());
        if (map == null || map.isEmpty()) {
            sendToOtherUsers(session, new TextMessage(String.format("User '%s' left", session.getPrincipal().getName())));
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws InterruptedException, IOException {
        sendToOtherSessions(session, new TextMessage(String.format(
            "<strong>%s:</strong> %s",
            session.getPrincipal().getName(),
            message.getPayload()
        )));
    }

    private void sendToOtherUsers(WebSocketSession session, WebSocketMessage<?> message) throws IOException {
        Iterator<WebSocketSession> iterator = registry.all();
        while (iterator.hasNext()) {
            WebSocketSession otherSession = iterator.next();
            if (Objects.equals(session.getPrincipal().getName(), otherSession.getPrincipal().getName())) {
                continue;
            }

            otherSession.sendMessage(message);
        }
    }

    private void sendToOtherSessions(WebSocketSession session, WebSocketMessage<?> message) throws IOException {
        Iterator<WebSocketSession> iterator = registry.all();
        while (iterator.hasNext()) {
            WebSocketSession otherSession = iterator.next();
            if (Objects.equals(session.getId(), otherSession.getId())) {
                continue;
            }

            otherSession.sendMessage(message);
        }
    }
}
