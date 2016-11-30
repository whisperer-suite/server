package net.whisperersuite.server.messages;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.whisperersuite.server.events.messages.ChatEvent;
import net.whisperersuite.server.websocket.MessageHandler;
import net.whisperersuite.server.websocket.WebSocketSessionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Iterator;

@Component @Scope("singleton")
public class MessageService {
    private final WebSocketSessionRegistry registry;
    private final ObjectMapper objectMapper;

    @Autowired
    public MessageService(WebSocketSessionRegistry registry, ObjectMapper objectMapper) {
        this.registry = registry;
        this.objectMapper = objectMapper;
    }

    public void sendToAllUsersExcept(String username, ChatEvent chatEvent) throws IOException {
        TextMessage textMessage = createTextMessage(chatEvent);

        Iterator<WebSocketSession> iterator = registry.all();
        while (iterator.hasNext()) {
            WebSocketSession otherSession = iterator.next();
            if (!otherSession.isOpen() || otherSession.getPrincipal().getName().equals(username)) {
                continue;
            }

            try {
                otherSession.sendMessage(textMessage);
            } catch (IOException ignored) {}
        }
    }

    public void sendToAllSessionExcept(WebSocketSession session, ChatEvent chatEvent) throws IOException {
        TextMessage textMessage = createTextMessage(chatEvent);

        Iterator<WebSocketSession> iterator = registry.all();
        while (iterator.hasNext()) {
            WebSocketSession otherSession = iterator.next();
            if (!otherSession.isOpen() || otherSession.getId().equals(session.getId())) {
                continue;
            }

            try {
                otherSession.sendMessage(textMessage);
            } catch (IOException ignored) {}
        }
    }

    private TextMessage createTextMessage(ChatEvent chatEvent) throws IOException {
        MessageHandler.JsonEvent jsonEvent = new MessageHandler.JsonEvent();
        jsonEvent.setType(chatEvent.eventType());
        jsonEvent.setPayload(objectMapper.readTree(objectMapper.writeValueAsString(chatEvent)));

        return new TextMessage(objectMapper.writeValueAsString(jsonEvent));
    }
}
