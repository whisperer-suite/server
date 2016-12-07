package net.whisperersuite.server.listener.connection;

import net.whisperersuite.server.event.connection.ConnectionEstablishedEvent;
import net.whisperersuite.server.payload.user.UserJoined;
import net.whisperersuite.server.service.PayloadService;
import net.whisperersuite.server.websocket.WebSocketSessionRegistry;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.socket.WebSocketSession;

import java.security.Principal;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class NotifyOtherUsersThatUserHasJoinedListenerTest {
    private static final String USER_NAME = "test_user";

    private WebSocketSessionRegistry registry;
    private PayloadService service;
    private NotifyOtherUsersThatUserHasJoinedListener listener;
    private WebSocketSession session;

    @Before
    public void setUp() {
        registry = new WebSocketSessionRegistry();
        service = mock(PayloadService.class);
        listener = new NotifyOtherUsersThatUserHasJoinedListener(registry, service);

        session = mock(WebSocketSession.class);
        Principal principal = mock(Principal.class);
        when(session.getPrincipal()).thenReturn(principal);
        when(principal.getName()).thenReturn(USER_NAME);
        when(session.getId()).thenReturn("1");
        registry.register(session, USER_NAME);
    }

    @Test
    public void handleEvent() throws Exception {
        assertNotNull(listener);
        listener.handleEvent(new ConnectionEstablishedEvent(session));

        verify(service, times(1)).sendToAllUsersExcept(eq(USER_NAME), any((UserJoined.class)));
    }

    @Test
    public void handleEventAlreadyIn() throws Exception {
        WebSocketSession session2 = mock(WebSocketSession.class);
        when(session2.getId()).thenReturn("2");
        registry.register(session2, USER_NAME);

        assertNotNull(listener);
        listener.handleEvent(new ConnectionEstablishedEvent(session));

        verify(service, never()).sendToAllUsersExcept(eq(USER_NAME), any((UserJoined.class)));
    }
}
