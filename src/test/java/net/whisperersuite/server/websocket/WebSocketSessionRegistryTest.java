package net.whisperersuite.server.websocket;

import net.whisperersuite.server.websocket.registry.WebSocketSessionRegistry;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.socket.WebSocketSession;

import java.security.Principal;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WebSocketSessionRegistryTest {
    private WebSocketSessionRegistry registry;

    @Before
    public void setUp() {
        registry = new WebSocketSessionRegistry();
    }

    @Test
    public void allRegisterUnregister() throws Exception {
        WebSocketSession session1 = mock(WebSocketSession.class);
        WebSocketSession session2 = mock(WebSocketSession.class);

        registry.register(session1);
        registry.register(session2);

        Collection<WebSocketSession> allSessions = registry.all();
        assertEquals(2, allSessions.size());
        assertTrue(allSessions.contains(session1));
        assertTrue(allSessions.contains(session2));

        registry.unregister(session1);

        allSessions = registry.all();
        assertEquals(1, allSessions.size());
        assertTrue(allSessions.contains(session2));
    }

    @Test
    public void getByPrincipalName() throws Exception {
        WebSocketSession session1 = mock(WebSocketSession.class);
        WebSocketSession session2 = mock(WebSocketSession.class);
        WebSocketSession session3 = mock(WebSocketSession.class);
        WebSocketSession session4 = mock(WebSocketSession.class);

        Principal principal1 = mock(Principal.class);
        Principal principal2 = mock(Principal.class);

        when(principal1.getName()).thenReturn("user1");
        when(principal2.getName()).thenReturn("user2");

        when(session1.getPrincipal()).thenReturn(principal1);
        when(session2.getPrincipal()).thenReturn(principal2);
        when(session3.getPrincipal()).thenReturn(principal1);
        when(session4.getPrincipal()).thenReturn(null);

        registry.register(session1);
        registry.register(session2);
        registry.register(session3);
        registry.register(session4);

        Collection<WebSocketSession> sessions = registry.getByPrincipalName("user1");
        assertEquals(2, sessions.size());
        assertTrue(sessions.contains(session1));
        assertTrue(sessions.contains(session3));
    }
}