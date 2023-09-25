package graph.utils;

import java.util.ArrayList;

public class ComparableList extends ArrayList<Integer> implements Comparable<ComparableList> {

    @Override
    public int compareTo(ComparableList o) {
        if (o != null) {
            final int shorterCodeLength = Math.min(size(), o.size());
            for (int i=0; i<shorterCodeLength; i++) {
                if (o.get(i) < get(i)) return 1;
                if (o.get(i) > get(i)) return -1;
            }

            return Integer.compare(size(), o.size());
        }
        throw new IllegalArgumentException("Only ComparableLists can be compared.");
    }
}
