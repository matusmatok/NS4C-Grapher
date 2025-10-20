package graph.components;

import lombok.Getter;

public class EdgePair {
    @Getter
    private final Edge e1, e2;
    public EdgePair(final Edge e1, final Edge e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof EdgePair) {
            final EdgePair o = (EdgePair) other;
            return (e1 == o.e1 && e2 == o.e2) || (e1 == o.e2 && e2 == o.e1);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return e1.hashCode() + e2.hashCode();
    }

    @Override
    public String toString() {
        return e1.getId() < e2.getId() ? e1.getId() + " " + e2.getId() : e2.getId() + " " + e1.getId();
    }
}
