package net.whisperersuite.server.payload;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AbstractPayloadTest {
    @Test
    public void getType() throws Exception {
        TestPayloadWithLongName payload = new TestPayloadWithLongName();

        assertEquals("TEST_PAYLOAD_WITH_LONG_NAME", payload.getType());
    }

    private static class TestPayloadWithLongName extends AbstractPayload {}
}
