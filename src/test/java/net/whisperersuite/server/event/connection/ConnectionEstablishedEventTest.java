package net.whisperersuite.server.event.connection;

import org.junit.Test;
import org.springframework.web.socket.WebSocketSession;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class ConnectionEstablishedEventTest {
    @Test
    public void constructor() throws Exception {
        assertNotNull(new ConnectionEstablishedEvent(mock(WebSocketSession.class)));
    }
}
