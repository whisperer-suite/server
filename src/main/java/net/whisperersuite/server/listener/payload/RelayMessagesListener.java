package net.whisperersuite.server.listener.payload;

import net.whisperersuite.server.event.payload.PayloadReceivedEvent;
import net.whisperersuite.server.payload.message.Message;
import net.whisperersuite.server.service.PayloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RelayMessagesListener {
    private final PayloadService payloadService;

    @Autowired
    public RelayMessagesListener(PayloadService payloadService) {
        this.payloadService = payloadService;
    }

    @EventListener
    public void handleEvent(PayloadReceivedEvent event) throws IOException {
        if (!(event.getPayload() instanceof Message)) {
            return;
        }

        payloadService.sendToAllSessionExcept(event.getSession(), event.getPayload());
    }
}
