package net.whisperersuite.server.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.whisperersuite.server.events.connection.ConnectionClosedEvent;
import net.whisperersuite.server.events.connection.ConnectionEstablishedEvent;
import net.whisperersuite.server.events.messages.Message;
import net.whisperersuite.server.events.messages.MessageSent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Component
public class MessageHandler extends TextWebSocketHandler {
    private static final Logger log = LoggerFactory.getLogger(MessageHandler.class);

    private final WebSocketSessionRegistry registry;
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public MessageHandler(WebSocketSessionRegistry registry, ObjectMapper objectMapper, ApplicationEventPublisher eventPublisher) {
        this.registry = registry;
        this.objectMapper = objectMapper;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("User '{}' connected", session.getPrincipal().getName());
        registry.register(session, session.getPrincipal().getName());
        eventPublisher.publishEvent(new ConnectionEstablishedEvent(session));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("User '{}' disconnected: {}", session.getPrincipal().getName(), status.getReason());
        registry.unregister(session);
        eventPublisher.publishEvent(new ConnectionClosedEvent(session));
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws InterruptedException, IOException {
        JsonEvent jsonEvent = objectMapper.readValue(message.getPayload(), JsonEvent.class);
        switch (jsonEvent.getType()) {
            case "MESSAGE":
                Message msg = objectMapper.readerFor(Message.class).readValue(jsonEvent.getPayload());
                msg.setAuthor(session.getPrincipal().getName());
                eventPublisher.publishEvent(new MessageSent(session, msg));
                break;
            default:
                throw new UnsupportedOperationException(jsonEvent.getType());
        }
    }

    public static class JsonEvent {
        private String type;
        private JsonNode payload;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public JsonNode getPayload() {
            return payload;
        }

        public void setPayload(JsonNode payload) {
            this.payload = payload;
        }
    }
}
