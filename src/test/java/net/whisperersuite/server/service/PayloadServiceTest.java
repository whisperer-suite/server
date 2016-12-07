package net.whisperersuite.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.whisperersuite.server.payload.AbstractPayload;
import net.whisperersuite.server.websocket.registry.WebSocketSessionRegistry;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.security.Principal;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class PayloadServiceTest {
    private WebSocketSessionRegistry registry = new WebSocketSessionRegistry();
    private PayloadService service;
    private WebSocketSession session1;
    private WebSocketSession session2;
    private WebSocketSession session3;
    private WebSocketSession session4;
    private AbstractPayload payload;

    @Before
    public void setUp() throws Exception {
        ObjectMapper mapper = mock(ObjectMapper.class);
        when(mapper.writeValueAsString(any(AbstractPayload.class))).thenReturn("{}");

        session1 = mockWebSocketSession(1, "user1");
        registry.register(session1);

        session2 = mockWebSocketSession(2, "user1");
        registry.register(session2);

        session3 = mockWebSocketSession(3, "user2");
        registry.register(session3);

        session4 = mockWebSocketSession(4, "user2");
        registry.register(session4);

        service = new PayloadService(registry, mapper);
        payload = mock(AbstractPayload.class);
    }

    @Test
    public void sendToAllUsersExcept() throws Exception {
        assertNotNull(service);
        service.sendToAllUsersExcept("user1", payload);

        verify(session1, never()).sendMessage(any(WebSocketMessage.class));
        verify(session2, never()).sendMessage(any(WebSocketMessage.class));
        verify(session3, times(1)).sendMessage(any(WebSocketMessage.class));
        verify(session4, times(1)).sendMessage(any(WebSocketMessage.class));

    }

    @Test
    public void sendToAllSessionExcept() throws Exception {
        assertNotNull(service);
        service.sendToAllSessionExcept(session1, payload);

        verify(session1, never()).sendMessage(any(WebSocketMessage.class));
        verify(session2, times(1)).sendMessage(any(WebSocketMessage.class));
        verify(session3, times(1)).sendMessage(any(WebSocketMessage.class));
        verify(session4, times(1)).sendMessage(any(WebSocketMessage.class));
    }

    private static WebSocketSession mockWebSocketSession(int i, String username) {
        WebSocketSession session = mock(WebSocketSession.class);
        when(session.getId()).thenReturn(String.valueOf(i));
        when(session.isOpen()).thenReturn(true);

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(username);

        when(session.getPrincipal()).thenReturn(principal);

        return session;
    }
}
