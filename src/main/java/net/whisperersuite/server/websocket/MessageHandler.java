package net.whisperersuite.server.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.whisperersuite.server.event.connection.ConnectionClosedEvent;
import net.whisperersuite.server.event.connection.ConnectionEstablishedEvent;
import net.whisperersuite.server.event.payload.PayloadReceivedEvent;
import net.whisperersuite.server.payload.AbstractPayload;
import net.whisperersuite.server.payload.message.Message;
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
        Class<? extends AbstractPayload> payloadClass = getPayloadClass(jsonEvent.getType());
        PayloadReceivedEvent<? extends AbstractPayload> event = createReceivedEvent(payloadClass, jsonEvent.getPayload(), session);
        eventPublisher.publishEvent(event);
    }

    private Class<? extends AbstractPayload> getPayloadClass(String typeName) {
        switch (typeName) {
            case "MESSAGE":
                return Message.class;
            default:
                throw new UnsupportedOperationException(typeName);
        }
    }

    private <P extends AbstractPayload> PayloadReceivedEvent<P> createReceivedEvent(
        Class<P> payloadClass,
        JsonNode jsonPayload,
        WebSocketSession session
    ) throws IOException {
        P payload = objectMapper.readerFor(payloadClass).readValue(jsonPayload);
        if (payload instanceof Message) {
            ((Message) payload).setAuthor(session.getPrincipal().getName());
        }

        return new PayloadReceivedEvent<>(session, payload);
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
