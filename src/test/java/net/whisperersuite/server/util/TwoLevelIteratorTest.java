package net.whisperersuite.server.util;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TwoLevelIteratorTest {
    @Test
    public void next() throws Exception {
        List<Iterable<String>> values = Arrays.<Iterable<String>>asList(
            Collections.singletonList("a"),
            Arrays.asList("b", "c"),
            Collections.<String>emptyList(),
            Collections.singletonList("d")
        );

        Iterator<String> iterator = new TwoLevelIterator<>(values, new TestFunction<String>());

        assertTrue(iterator.hasNext());
        assertEquals("a", iterator.next());

        assertTrue(iterator.hasNext());
        assertEquals("b", iterator.next());

        assertTrue(iterator.hasNext());
        assertEquals("c", iterator.next());

        assertTrue(iterator.hasNext());
        assertEquals("d", iterator.next());

        assertFalse(iterator.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void nextEmpty() throws Exception {
        Iterator<String> iterator = new TwoLevelIterator<>(
            Collections.<Iterable<String>>emptyIterator(),
            new TestFunction<String>()
        );

        iterator.next();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void remove() throws Exception {
        Iterator<String> iterator = new TwoLevelIterator<>(
            Collections.<Iterable<String>>emptyIterator(),
            new TestFunction<String>()
        );

        iterator.remove();
    }

    private static class TestFunction <T> implements TwoLevelIterator.Function<Iterable<T>, T> {
        @Override
        public Iterator<T> getIterator(Iterable<T> value) {
            return value.iterator();
        }
    }
}