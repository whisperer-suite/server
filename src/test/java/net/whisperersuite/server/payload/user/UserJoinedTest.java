package net.whisperersuite.server.payload.user;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserJoinedTest {
    @Test
    public void getType() throws Exception {
        assertEquals("USER_JOINED", new UserJoined("test").getType());
    }
}
