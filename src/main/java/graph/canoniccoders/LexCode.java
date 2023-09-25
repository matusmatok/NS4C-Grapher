package graph.canoniccoders;

import graph.utils.ComparableList;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class LexCode implements Comparable<LexCode> {
    private final ComparableList shortCode;
    private final ComparableList extendedCode;
    private final int hash;

    private LexCode(final ComparableList shortCode, final ComparableList extendedCode) {
        this.shortCode = shortCode;
        this.extendedCode = extendedCode;
        this.hash = Objects.hash(shortCode.addAll(extendedCode));
    }

    public static LexCodeBuilder builder() {
        return new LexCodeBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof LexCode) {
            return compareTo((LexCode) o) == 0;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    public int shortCodeAt(final int index) {
        return shortCode.get(index);
    }

    public int extendedCodeAt(final int index) {
        return shortCode.get(index);
    }

    public int shortSize() {
        return extendedCode.size();
    }

    public int extendedSize() {
        return extendedCode.size();
    }

    @Override
    public int compareTo(LexCode o) {
        final int shortComparisonResult = shortCode.compareTo(o.shortCode);
        if ((shortComparisonResult == 0) && (extendedCode != null)) {
            return extendedCode.compareTo(o.extendedCode);
        }
        return shortComparisonResult;
    }

    @Override
    public String toString() {
        return shortCode.stream().map(Object::toString).collect(Collectors.joining(" "));
    }

    public static class LexCodeBuilder {
        private final ComparableList shortCode = new ComparableList();
        private final ComparableList extendedCode = new ComparableList();

        public LexCodeBuilder shortAddAll(final List<Integer> faceSizes) {
            shortCode.addAll(faceSizes);
            return this;
        }

        public LexCodeBuilder shortAdd(final int faceSize) {
            shortCode.add(faceSize);
            return this;
        }

        public LexCodeBuilder extendedAddAll(final List<Integer> faceSizes) {
            extendedCode.addAll(faceSizes);
            return this;
        }

        public LexCodeBuilder extendedAdd(final int faceSize) {
            extendedCode.add(faceSize);
            return this;
        }

        public LexCode build() {
            return new LexCode(shortCode, extendedCode);
        }
    }


}
