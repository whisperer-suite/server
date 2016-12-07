package net.whisperersuite.server.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.whisperersuite.server.event.connection.ConnectionClosedEvent;
import net.whisperersuite.server.event.connection.ConnectionEstablishedEvent;
import net.whisperersuite.server.event.payload.PayloadReceivedEvent;
import net.whisperersuite.server.payload.message.Message;
import net.whisperersuite.server.websocket.registry.WebSocketSessionRegistry;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.security.Principal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.web.socket.CloseStatus.NORMAL;

public class MessageHandlerTest {
    private WebSocketSessionRegistry registry;
    private ApplicationEventPublisher eventPublisher;
    private MessageHandler handler;
    private WebSocketSession session;

    @Before
    public void setUp() {
        registry = mock(WebSocketSessionRegistry.class);
        eventPublisher = mock(ApplicationEventPublisher.class);
        handler = new MessageHandler(registry, new ObjectMapper(), eventPublisher);
        session = mock(WebSocketSession.class);

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("test_user");
        when(session.getPrincipal()).thenReturn(principal);
    }

    @Test
    public void afterConnectionEstablished() throws Exception {
        assertNotNull(handler);
        handler.afterConnectionEstablished(session);

        verify(registry, times(1)).register(same(session));
        verify(eventPublisher, times(1)).publishEvent(any(ConnectionEstablishedEvent.class));
    }

    @Test
    public void afterConnectionClosed() throws Exception {
        assertNotNull(handler);
        handler.afterConnectionClosed(session, NORMAL);

        verify(registry, times(1)).unregister(same(session));
        verify(eventPublisher, times(1)).publishEvent(any(ConnectionClosedEvent.class));
    }

    @Test
    public void handleTextMessage() throws Exception {
        final TextMessage message = new TextMessage("{\"type\":\"MESSAGE\",\"payload\":{\"content\":\"hi\"}}");

        assertNotNull(handler);
        handler.handleTextMessage(session, message);

        ArgumentCaptor<PayloadReceivedEvent> argument = ArgumentCaptor.forClass(PayloadReceivedEvent.class);
        verify(eventPublisher, times(1)).publishEvent(argument.capture());
        assertTrue(argument.getValue().getPayload() instanceof Message);
        assertEquals("hi", ((Message) argument.getValue().getPayload()).getContent());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void handleTextMessageUnsupportedType() throws Exception {
        final TextMessage message = new TextMessage("{\"type\":\"NOT_SUPPERTED\",\"payload\":{}}");

        assertNotNull(handler);
        handler.handleTextMessage(session, message);
    }
}
