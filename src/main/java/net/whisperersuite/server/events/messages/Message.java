package net.whisperersuite.server.events.messages;

public class Message extends ChatEvent {
    private String content;
    private String author;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String eventType() {
        return "MESSAGE";
    }
}
