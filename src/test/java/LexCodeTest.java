import graph.canoniccoders.CachingCoder;
import graph.canoniccoders.CanonicalCoder;
import graph.canoniccoders.LexCode;
import graph.components.*;
import graph.utils.EasyList;
import mock.FacedTutteFragmentMock;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

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

    @Test
    public void testNoLadderExtension() {
        final List<Integer> expectedLong = EasyList.of(
                4, 5, 5, 4, 5, 5,
                0, 0, 5, 0, 0,
                0, 0, 0, 5, 0,
                0, 0, 0, 0,
                0, 0, 0, 4, 0);
        final List<Integer> expectedShort = EasyList.of(4, 5, 5, 4, 5, 5, 5, 5, 4);
        final LexCode expectedCode = LexCode.builder().shortAddAll(expectedShort).extendedAddAll(expectedLong).build();

        final AbstractGraph noLadderGraph = new NoLadderC4ECGraph();
        noLadderGraph.init();

        CanonicalCoder.noLadderExtension = true;

        final LexCode actualCode = CanonicalCoder.getCodeForGraph(noLadderGraph);
        assertEquals(expectedCode, actualCode);

        CanonicalCoder.noLadderExtension = false;
    }

    @Test
    public void testCachingCoder() {
        final CachingCoder cachingCoder = new CachingCoder(graph);

        final LexCode cachedLexCode1 = cachingCoder.getGraphCode();
        final LexCode lexCode1 = CanonicalCoder.getCodeForGraph(graph);
        assertEquals(lexCode1, cachedLexCode1);

        final LexCode cachedLecCode2 = cachingCoder.getLexCode(graph.getEdge(4));
        final LexCode lexCode2 = CanonicalCoder.getCodeForEdge(graph, graph.getEdge(4));
        assertEquals(lexCode2, cachedLecCode2);
    }

    @Test
    public void testVertexCaching() {
        final CachingCoder cachingCoder = new CachingCoder(graph);
        final Face cube = graph.getFaces().stream()
                .filter(face -> face.size() == 4)
                .findAny()
                .orElseThrow();
        final Edge borderToSix = cube.getAdjacentEdges()
                .stream().filter(edge -> edge.getOppositeFace(cube).size() == 6)
                .findAny()
                .orElseThrow();

        final LexCode leftCode = cachingCoder.getLexCode(cube, borderToSix, borderToSix.getLeftFromPOVofFace(cube));
        final LexCode rightCode = cachingCoder.getLexCode(cube, borderToSix, borderToSix.getRightFromPOVofFace(cube));
        assertEquals(leftCode, rightCode);
        assertTrue(leftCode.shortStartsAs(4, 6, 5));
        assertTrue(cachingCoder.isEdgeVertexTransitive(borderToSix));
    }

    @Test
    public void testCanonicalMaps() {
        final CachingCoder cachingCoder = new CachingCoder(graph);
        final Map<Edge, Vertex> canonicalMap = cachingCoder.getCanonicalEdgeAndVertex(graph.getFace(0));

        assertEquals(2, canonicalMap.size());
        final Edge firstCanonicalEdge = graph.getEdge(0, 2);
        final Edge secondCanonicalEdge = graph.getEdge(1, 3);
        assertTrue(canonicalMap.containsKey(firstCanonicalEdge));
        assertTrue(canonicalMap.containsKey(secondCanonicalEdge));
        assertEquals(graph.getVertex(2), canonicalMap.get(firstCanonicalEdge));
        assertEquals(graph.getVertex(3), canonicalMap.get(secondCanonicalEdge));
    }
}
