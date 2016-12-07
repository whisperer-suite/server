package net.whisperersuite.server.websocket.registry;

public interface Predicate<T> {
    boolean test(T t);
}
