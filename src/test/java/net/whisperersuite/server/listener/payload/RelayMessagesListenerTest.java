package net.whisperersuite.server.listener.payload;

import net.whisperersuite.server.event.payload.PayloadReceivedEvent;
import net.whisperersuite.server.payload.AbstractPayload;
import net.whisperersuite.server.payload.message.Message;
import net.whisperersuite.server.service.PayloadService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.socket.WebSocketSession;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.*;

public class RelayMessagesListenerTest {
    private PayloadService service;
    private RelayMessagesListener listener;

    @Before
    public void setUp() {
        service = mock(PayloadService.class);
        listener = new RelayMessagesListener(service);
    }

    @Test
    public void handleEvent() throws Exception {
        WebSocketSession session = mock(WebSocketSession.class);
        Message message = mock(Message.class);
        PayloadReceivedEvent<Message> event = new PayloadReceivedEvent<>(session, message);

        assertNotNull(listener);
        listener.handleEvent(event);

        verify(service).sendToAllSessionExcept(same(session), same(message));
    }

    @Test
    public void handleEventNotMessage() throws Exception {
        WebSocketSession session = mock(WebSocketSession.class);
        AbstractPayload notMessage = mock(AbstractPayload.class);
        PayloadReceivedEvent<AbstractPayload> event = new PayloadReceivedEvent<>(session, notMessage);

        assertNotNull(listener);
        listener.handleEvent(event);

        verify(service, never()).sendToAllSessionExcept(any(WebSocketSession.class), any(AbstractPayload.class));
    }
}
