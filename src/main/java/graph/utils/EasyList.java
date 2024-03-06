package graph.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class EasyList<T> extends ArrayList<T> {

    public EasyList() {
        super();
    }

    public EasyList(final int initialSize) {
        super(initialSize);
    }

    @SafeVarargs
    public static <T> EasyList<T> of(T... e) {
        final EasyList<T> result = new EasyList<>(e.length + 1);
        result.addAll(Arrays.asList(e));
        return result;
    }

    public static <T> EasyList<T> copyOf(Collection<T> collection) {
        final EasyList<T> result = new EasyList<>(collection.size() + 1);
        result.addAll(collection);
        return result;
    }
}
