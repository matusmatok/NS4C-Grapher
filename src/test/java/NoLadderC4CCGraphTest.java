import static org.junit.Assert.assertEquals;

import graph.canoniccoders.CanonicalCoder;
import graph.canoniccoders.LexCode;
import graph.components.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.List;

public class NoLadderC4CCGraphTest {
    private static NoLadderC4ECGraph graph;
    private static LexCode lexCode;
    private static String baseGraphString;

    @BeforeClass
    public static void setup() {
        final List<Integer> extendedCode = List.of(4,
                5, 5, 5, 5,
                0, 0, 4, 5, 0,
                0, 0, 0, 4, 0,
                0, 0, 0, 5, 0);
        final List<Integer> shortCode = List.of(4,5,5,5,5,4,5,4,5);
        lexCode = LexCode.builder().shortAddAll(shortCode).extendedAddAll(extendedCode).build();

        final AbstractGraph baseGraph = new NoLadderC4ECGraph();
        baseGraph.init();
        baseGraphString = baseGraph.toString();
    }

    @Before
    public void before() {
        graph = new NoLadderC4ECGraph();
        graph.init();
    }

    @Test
    public void testBaseGraphCorrectness() {
        final LexCode code = CanonicalCoder.getCodeForGraph(graph);
        assertEquals(lexCode, code);
    }
    @Test
    public void testOperationA() {
        final Face setupParameterFace = graph.getFace(3);
        final Edge setupParameterEdge = setupParameterFace.getEdge(2);
        graph.operationB(setupParameterFace, setupParameterEdge);
        final LexCode preOperationACode = CanonicalCoder.getCodeForGraph(graph);

        final Face newSquare = graph.getFace(graph.getFaces().size() - 1);
        final Edge leftEdge = graph.getEdge(graph.getEdges().size() - 1);
        final Vertex originVertex = graph.getVertex(graph.getVertices().size() - 1);
        final Edge.OrientedEdge orientedEdge = leftEdge.getOriented(originVertex);
        graph.operationA(orientedEdge);

        final LexCode newLexCode = CanonicalCoder.getCodeForFace(graph, graph.getFace(graph.getFaces().size() - 2));
        assertEquals("4 5 5 5 7", newLexCode.toString().substring(0, 9));

        graph.reductionA();
        assertEquals(preOperationACode, CanonicalCoder.getCodeForGraph(graph));
    }

    @Test
    public void testOperationB() {
        final Face parameterFace = graph.getFace(3);
        final Edge parameterEdge = parameterFace.getAdjacentEdges().get(2);

        graph.operationB(parameterFace, parameterEdge);
        final LexCode postOperationCode = CanonicalCoder.getCodeForGraph(graph);
        assertEquals("4 5 5 6", postOperationCode.toString().substring(0,7));

        graph.reductionB(parameterFace, parameterEdge);
        final LexCode postReductionCode = CanonicalCoder.getCodeForGraph(graph);
        assertEquals(baseGraphString, graph.toString());
        assertEquals(lexCode, postReductionCode);
    }

    @Test
    public void testOperationC() {
        final Face setupParameterFace = graph.getFace(3);
        graph.operationD(setupParameterFace);
        final LexCode preOperationCCode = CanonicalCoder.getCodeForGraph(graph);

        final Face newSquare = graph.getFace(graph.getFaces().size() - 1);
        final Vertex anyAdjacentVertex = newSquare.getEdge(0).getVFrom();
        final Edge.OrientedEdge orientedEdge = anyAdjacentVertex.getOppositeEdge(newSquare).getOriented(anyAdjacentVertex);

        graph.operationC(orientedEdge);
        final LexCode newLexCode = CanonicalCoder.getCodeForFace(graph, newSquare);
        assertEquals("4 5 5 5 5 5", newLexCode.toString().substring(0, 11));

        graph.reductionC();
        assertEquals(preOperationCCode, CanonicalCoder.getCodeForGraph(graph));
    }

    @Test
    public void testOperationD() {
        final Face parameterFace = graph.getFace(3);

        graph.operationD(parameterFace);
        final LexCode postOperationCode = CanonicalCoder.getCodeForGraph(graph);
        assertEquals("4 5 5 5 5", postOperationCode.toString().substring(0,9));

        graph.reductionD(parameterFace);
        final LexCode postReductionCode = CanonicalCoder.getCodeForGraph(graph);
        assertEquals(lexCode, postReductionCode);
    }
}
