package net.whisperersuite.server.jackson;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class ObjectMapperProviderTest {
    @Test
    public void getObjectMapper() throws Exception {
        assertNotNull(new ObjectMapperProvider().getObjectMapper());
    }
}
