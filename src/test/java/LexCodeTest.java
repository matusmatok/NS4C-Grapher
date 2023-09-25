import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import graph.canoniccoders.CanonicalCoder;
import graph.canoniccoders.LexCode;
import graph.components.Face;
import graph.components.Graph;
import mock.FacedTutteFragmentMock;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class LexCodeTest {
    private static final Graph graph = new FacedTutteFragmentMock();
    private static LexCode lexCode;
    private static LexCode withoutExtendedLexCode;

    @BeforeClass
    public static void setup() {
        final List<Integer> faceSizes = List.of(4,5,5,5,6,4,5,5,4,5);
        final int stub = CanonicalCoder.CODE_FOR_VISITED;
        final List<Integer> extendedFaceSizes = List.of(4,
                5, 5, 5, 6,
                stub, stub, 4 , 5, stub,
                stub, stub, stub, 5, stub,
                stub, stub, stub, 4, stub,
                stub, stub, stub, 5, stub, stub);
        lexCode = LexCode.builder().shortAddAll(faceSizes).extendedAddAll(extendedFaceSizes).build();

        withoutExtendedLexCode = LexCode.builder().shortAddAll(faceSizes).build();
    }

    @Test
    public void testLexCodeComparisons() {
        assertEquals(lexCode, lexCode);

        assertNotEquals(lexCode, withoutExtendedLexCode);
        assertNotEquals(withoutExtendedLexCode, lexCode);

        assertEquals(withoutExtendedLexCode, withoutExtendedLexCode);
    }

    @Test
    public void testLexCodeConstruction() {
        final LexCode tutteFragmentLexCode = CanonicalCoder.lexCodeForGraph(graph);
        assertEquals(lexCode, tutteFragmentLexCode);
        final Face square = graph.getFaces().stream().filter(f -> f.size() == 4).findAny().orElseThrow();
        final LexCode squareOriginatedCode = CanonicalCoder.getCodeForFace(graph,square);
        assertEquals(squareOriginatedCode, tutteFragmentLexCode);
    }
}
