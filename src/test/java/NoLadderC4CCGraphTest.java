import static org.junit.Assert.assertEquals;

import graph.canoniccoders.CanonicalCoder;
import graph.canoniccoders.LexCode;
import graph.components.NoLadderC4ECGraph;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.List;

public class NoLadderC4CCGraphTest {
    private static final NoLadderC4ECGraph graph = new NoLadderC4ECGraph();
    private static LexCode lexCode;

    @BeforeClass
    public static void setup() {
        final List<Integer> extendedCode = List.of(4,
                5, 5, 5, 5,
                0, 0, 4, 5, 0,
                0, 0, 0, 4, 0,
                0, 0, 0, 5, 0);
        final List<Integer> shortCode = List.of(4,5,5,5,5,4,5,4,5);
        lexCode = LexCode.builder().shortAddAll(shortCode).extendedAddAll(extendedCode).build();
    }

    @Test
    public void testIfLexCodeComputes() {
        graph.init();
        final LexCode code = CanonicalCoder.getCodeForGraph(graph);
        assertEquals(lexCode, code);
    }
}
