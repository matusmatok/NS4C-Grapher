import graph.components.NoLadderC4ECGraph;

import java.io.InputStreamReader;
import java.util.Objects;

public interface ResourceDependent {
    default InputStreamReader loadResourceAsStream(final String path) {
        return new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream(path)));
    }
}
