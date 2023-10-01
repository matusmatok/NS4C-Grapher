import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import graph.canoniccoders.CanonicalCoder;
import graph.canoniccoders.LexCode;
import graph.components.Face;
import graph.components.AbstractGraph;
import mock.FacedTutteFragmentMock;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class LexCodeTest {
    private static final AbstractGraph graph = new FacedTutteFragmentMock();
    private static LexCode lexCode;
    private static LexCode withoutExtendedLexCode;
    private static LexCode lexCodeForFaceAndEdge;
    private static LexCode lexCodeForVertex;

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

        final List<Integer> extendedCodeForFaceAndEdge = List.of(5,
                5, 4, 5, 5, 5,
                stub, stub, 4, 6, stub,
                stub, stub, stub, stub,
                stub, stub, stub, 4, stub,
                stub, stub, stub, 5, stub);
        final List<Integer> shortCodeForFaceAndEdge = List.of(5,5,4,5,5,5,4,6,4,5);
        lexCodeForFaceAndEdge = LexCode.builder().shortAddAll(shortCodeForFaceAndEdge).extendedAddAll(extendedCodeForFaceAndEdge)
                .build();

        final List<Integer> extendedCodeForVertex = List.of(5,
                5, 5, 5, 4, 5,
                stub, stub, 4, 5, stub,
                stub, stub, stub, 4, stub,
                stub, stub, stub, 6, stub);
        final List<Integer> shortCodeForVertex = List.of(5,5,5,5,4,5,4,5,4,6);
        lexCodeForVertex = LexCode.builder().shortAddAll(shortCodeForVertex).extendedAddAll(extendedCodeForVertex).build();
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
        final LexCode tutteFragmentLexCode = CanonicalCoder.getCodeForGraph(graph);
        assertEquals(lexCode, tutteFragmentLexCode);
        final Face square = graph.getFaces().stream().filter(f -> f.size() == 4).findAny().orElseThrow();
        final LexCode squareOriginatedCode = CanonicalCoder.getCodeForFace(graph,square);
        assertEquals(squareOriginatedCode, tutteFragmentLexCode);
    }

    @Test
    public void testGetCodeForEdgeAndFace() {
        final LexCode actualLexCode = CanonicalCoder.getCodeForFaceAndEdge(graph, graph.getFaces().get(2), graph.getEdges().get(6));
        assertEquals(lexCodeForFaceAndEdge, actualLexCode);
    }

    @Test
    public void testGetCodeForEdge() {
        final LexCode actualLexCode = CanonicalCoder.getCodeForEdge(graph, graph.getEdges().get(6));
        assertEquals(lexCodeForFaceAndEdge, actualLexCode);
    }

    @Test
    public void testGetCodeForVertex() {
        final LexCode actualLexCode1 = CanonicalCoder.getCodeForVertex(graph, graph.getVertices().get(7));
        assertEquals(lexCodeForVertex, actualLexCode1);

        final LexCode actualLexCode2 = CanonicalCoder.getCodeForVertex(graph, graph.getVertices().get(2));
        assertEquals(lexCode, actualLexCode2);
    }
}
