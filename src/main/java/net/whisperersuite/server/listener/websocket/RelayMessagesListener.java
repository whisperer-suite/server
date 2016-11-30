package net.whisperersuite.server.listener.websocket;

import net.whisperersuite.server.events.messages.MessageSent;
import net.whisperersuite.server.messages.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RelayMessagesListener {
    private final MessageService messageService;

    @Autowired
    public RelayMessagesListener(MessageService messageService) {
        this.messageService = messageService;
    }

    @EventListener
    public void handleEvent(MessageSent event) throws IOException {
        messageService.sendToAllSessionExcept(event.getSession(), event.getMessage());
    }
}
