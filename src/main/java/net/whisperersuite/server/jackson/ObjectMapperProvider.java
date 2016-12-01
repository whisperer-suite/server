package net.whisperersuite.server.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

public class ObjectMapperProvider {
    @Bean @Scope("singleton")
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }
}
