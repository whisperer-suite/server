package net.whisperersuite.server.payload.message;

import org.junit.Test;

import static org.junit.Assert.*;

public class MessageTest {
    @Test
    public void getAndSetContent() throws Exception {
        Message message = new Message();
        message.setContent("test content");
        assertEquals("test content", message.getContent());
    }

    @Test
    public void getAuthor() throws Exception {
        Message message = new Message();
        message.setAuthor("test author");
        assertEquals("test author", message.getAuthor());
    }
}
