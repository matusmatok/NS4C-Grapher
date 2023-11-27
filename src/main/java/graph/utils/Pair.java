package graph.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Pair<U,T> {

    private U key;
    private T value;

    @Override
    public boolean equals(Object o) {
        if (o instanceof Pair) {
            final Pair<U,T> other = (Pair<U, T>) o;
            return (this.key.equals(other.key) && this.value.equals(other.value));
        }
        return false;
    }
}
