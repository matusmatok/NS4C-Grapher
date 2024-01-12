package plantri;

import graph.components.NoLadderC4ECGraph;

import java.io.InputStreamReader;
import java.util.Iterator;

public abstract class AbstractFilter extends PlantriOutputReader{

    public AbstractFilter(InputStreamReader reader) {
        super(reader);
    }

    private class FilteringIterator implements Iterator<NoLadderC4ECGraph> {
        private NoLadderC4ECGraph next;
        private final Iterator<NoLadderC4ECGraph> parentIterator;

        private FilteringIterator(final Iterator<NoLadderC4ECGraph> parentIterator) {
            this.parentIterator = parentIterator;
            next = prepareNext();
        }

        private NoLadderC4ECGraph prepareNext() {
            while (parentIterator.hasNext()) {
                final NoLadderC4ECGraph currentGraph = parentIterator.next();
                if (filter(currentGraph)) {
                    return currentGraph;
                }
            }
            return null;
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public NoLadderC4ECGraph next() {
            final NoLadderC4ECGraph result = next;
            next = prepareNext();
            return result;
        }
    }

    public abstract boolean filter(final NoLadderC4ECGraph graph);

    @Override
    public Iterator<NoLadderC4ECGraph> iterator() {
        final Iterator<NoLadderC4ECGraph> parentIterator = super.iterator();
        return new FilteringIterator(parentIterator);
    }
}
