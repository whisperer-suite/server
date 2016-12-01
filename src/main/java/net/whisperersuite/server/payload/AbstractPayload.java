package net.whisperersuite.server.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class AbstractPayload {
    @JsonIgnore
    public String getType() {
        return getClass().getSimpleName().replaceAll("(.)([A-Z])", "$1_$2").toUpperCase();
    }
}
