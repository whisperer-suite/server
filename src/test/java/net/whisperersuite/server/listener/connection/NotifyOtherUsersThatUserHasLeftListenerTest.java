package net.whisperersuite.server.listener.connection;

import net.whisperersuite.server.event.connection.ConnectionClosedEvent;
import net.whisperersuite.server.payload.user.UserLeft;
import net.whisperersuite.server.service.PayloadService;
import net.whisperersuite.server.websocket.WebSocketSessionRegistry;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.socket.WebSocketSession;

import java.security.Principal;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class NotifyOtherUsersThatUserHasLeftListenerTest {
    private static final String USER_NAME = "test_user";

    private WebSocketSessionRegistry registry;
    private PayloadService service;
    private NotifyOtherUsersThatUserHasLeftListener listener;
    private WebSocketSession session;
    private WebSocketSession session2;

    @Before
    public void setUp() throws Exception {
        registry = new WebSocketSessionRegistry();
        service = mock(PayloadService.class);
        listener = new NotifyOtherUsersThatUserHasLeftListener(registry, service);

        session = mock(WebSocketSession.class);
        Principal principal = mock(Principal.class);
        when(session.getPrincipal()).thenReturn(principal);
        when(principal.getName()).thenReturn(USER_NAME);
        when(session.getId()).thenReturn("1");
        registry.register(session, USER_NAME);

        session2 = mock(WebSocketSession.class);
        when(session2.getId()).thenReturn("2");
        registry.register(session2, USER_NAME);
    }

    @Test
    public void handleEvent() throws Exception {
        registry.unregister(session2.getId());
        registry.unregister(session.getId());

        assertNotNull(listener);
        listener.handleEvent(new ConnectionClosedEvent(session));

        verify(service, times(1)).sendToAllUsersExcept(eq(USER_NAME), any(UserLeft.class));
    }

    @Test
    public void handleEventNotYetOut() throws Exception {
        registry.unregister(session.getId());

        assertNotNull(listener);
        listener.handleEvent(new ConnectionClosedEvent(session));

        verify(service, never()).sendToAllUsersExcept(eq(USER_NAME), any(UserLeft.class));
    }
}
