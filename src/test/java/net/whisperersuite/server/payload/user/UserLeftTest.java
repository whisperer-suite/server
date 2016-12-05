package net.whisperersuite.server.payload.user;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserLeftTest {
    @Test
    public void getType() throws Exception {
        assertEquals("USER_LEFT", new UserLeft("test").getType());
    }
}
