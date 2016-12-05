package net.whisperersuite.server.event;

import org.junit.Test;
import org.springframework.web.socket.WebSocketSession;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

public class WebSocketEventTest {
    @Test
    public void getSession() throws Exception {
        WebSocketSession session = mock(WebSocketSession.class);
        assertSame(session, new TestWebSocketEvent(session).getSession());
    }

    private static class TestWebSocketEvent extends WebSocketEvent {
        TestWebSocketEvent(WebSocketSession session) {
            super(session);
        }
    }
}
