package net.whisperersuite.server.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class TwoLevelIterator<T, U> implements Iterator<T> {
    private final Iterator<U> outerIterator;
    private final Function<U, T> innerIteratorProducer;
    private Iterator<T> currentIterator;

    public TwoLevelIterator(Iterator<U> outerIterator, Function<U, T> innerIteratorProducer) {
        this.outerIterator = outerIterator;
        this.innerIteratorProducer = innerIteratorProducer;
    }

    public TwoLevelIterator(Iterable<U> outerIterator, Function<U, T> innerIteratorProducer) {
        this(outerIterator.iterator(), innerIteratorProducer);
    }

    @Override
    public boolean hasNext() {
        if (currentIterator != null && currentIterator.hasNext()) {
            return true;
        }

        while (outerIterator.hasNext()) {
            currentIterator = innerIteratorProducer.getIterator(outerIterator.next());
            if (currentIterator != null && currentIterator.hasNext()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public T next() {
        if (hasNext()) {
            return currentIterator.next();
        }

        throw new NoSuchElementException();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove");
    }

    public interface Function<V, W> {
        Iterator<W> getIterator(V value);
    }
}
