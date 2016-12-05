package net.whisperersuite.server.event.payload;

import net.whisperersuite.server.payload.AbstractPayload;
import org.junit.Test;
import org.springframework.web.socket.WebSocketSession;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

public class PayloadReceivedEventTest {
    @Test
    public void getPayload() throws Exception {
        TestPayload payload = new TestPayload();
        assertSame(payload, new PayloadReceivedEvent<>(mock(WebSocketSession.class), payload).getPayload());
    }

    private static class TestPayload extends AbstractPayload {}
}
