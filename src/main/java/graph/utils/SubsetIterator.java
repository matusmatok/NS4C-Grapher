package graph.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SubsetIterator implements Iterator<List<Integer>> {
    private final int setSize;
    private final int subsetSize;
    private final List<Integer> currentSubset;
    private boolean hasNext = true;

    public SubsetIterator(final int setSize, final int subsetSize) {
        this.setSize = setSize;
        this.subsetSize = subsetSize;
        this.currentSubset = new ArrayList<>(subsetSize);
        for(int i = 0; i < subsetSize; i++) {
            currentSubset.add(i);
        }
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public List<Integer> next() {
        final List<Integer> res =  List.copyOf(currentSubset);
        increment(0);
        return res;
    }

    private int increment(final int positionFromBack) {
        if (positionFromBack >= subsetSize) {
            hasNext = false;
            return setSize - positionFromBack + 1;
        } else {
            final int arrayIndex = subsetSize - positionFromBack - 1;
            int newValue = currentSubset.get(arrayIndex) + 1;
            if (newValue < setSize - positionFromBack) {
                currentSubset.set(arrayIndex, newValue);
                return newValue;
            } else {
                newValue = increment(positionFromBack + 1) + 1;
                currentSubset.set(arrayIndex, newValue);
                return newValue;
            }
        }
    }
}
