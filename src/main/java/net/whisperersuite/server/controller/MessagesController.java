package net.whisperersuite.server.controller;

import net.whisperersuite.server.model.Greeting;
import net.whisperersuite.server.model.HelloMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessagesController {
    private static final Logger log = LoggerFactory.getLogger(MessagesController.class);

    @MessageMapping("/{channel}")
    @SendTo("/topic/greetings")
    public Greeting greeting(
        @DestinationVariable String channel,
        HelloMessage message
    ) throws Exception {
        log.info(
            "User '{}' sent message @ {} in channel '{}'",
            message.getName(),
            message.getCreatedAt(),
            channel
        );

        Thread.sleep(1000); // simulated delay

        return new Greeting("Hello, " + message.getName() + "!");
    }
}
