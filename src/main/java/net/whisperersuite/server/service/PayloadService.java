package net.whisperersuite.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.whisperersuite.server.payload.AbstractPayload;
import net.whisperersuite.server.websocket.MessageHandler;
import net.whisperersuite.server.websocket.registry.WebSocketSessionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Component @Scope("singleton")
public class PayloadService {
    private final WebSocketSessionRegistry registry;
    private final ObjectMapper objectMapper;

    @Autowired
    public PayloadService(WebSocketSessionRegistry registry, ObjectMapper objectMapper) {
        this.registry = registry;
        this.objectMapper = objectMapper;
    }

    public void sendToAllUsersExcept(String username, AbstractPayload payload) throws IOException {
        TextMessage textMessage = createTextMessage(payload);

        for (WebSocketSession otherSession : registry.all()) {
            if (!otherSession.isOpen() || otherSession.getPrincipal().getName().equals(username)) {
                continue;
            }

            try {
                otherSession.sendMessage(textMessage);
            } catch (IOException ignored) {}
        }
    }

    public void sendToAllSessionExcept(WebSocketSession session, AbstractPayload payload) throws IOException {
        TextMessage textMessage = createTextMessage(payload);

        for (WebSocketSession otherSession : registry.all()) {
            if (!otherSession.isOpen() || otherSession.getId().equals(session.getId())) {
                continue;
            }

            try {
                otherSession.sendMessage(textMessage);
            } catch (IOException ignored) {}
        }
    }

    private TextMessage createTextMessage(AbstractPayload payload) throws IOException {
        MessageHandler.JsonEvent jsonEvent = new MessageHandler.JsonEvent();
        jsonEvent.setType(payload.getType());
        jsonEvent.setPayload(objectMapper.readTree(objectMapper.writeValueAsString(payload)));

        return new TextMessage(objectMapper.writeValueAsString(jsonEvent));
    }
}
