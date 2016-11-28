package net.whisperersuite.server.model;

import java.util.Date;

public class HelloMessage {
    private String name;
    private Date createdAt;

    public HelloMessage() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
