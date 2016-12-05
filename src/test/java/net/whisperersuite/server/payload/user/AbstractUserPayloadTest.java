package net.whisperersuite.server.payload.user;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AbstractUserPayloadTest {
    @Test
    public void getUsername() throws Exception {
        AbstractUserPayload payload = new TestUserPayload("test user");

        assertEquals("test user", payload.getUsername());
    }

    private static class TestUserPayload extends AbstractUserPayload {
        TestUserPayload(String username) {
            super(username);
        }
    }
}
