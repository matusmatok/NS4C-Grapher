import static org.junit.Assert.assertEquals;

import graph.canoniccoders.CanonicalCoder;
import graph.canoniccoders.LexCode;
import graph.components.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.List;

public class NoLadderC4CCGraphTest implements ResourceDependent{
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
    public void testLoadFromFile() throws FileNotFoundException {
        final NoLadderC4ECGraph loadedGraph = NoLadderC4ECGraph.loadFromFile(loadResourceAsStream("base_graph.txt"));
        final LexCode lexCode1 = CanonicalCoder.getCodeForGraph(loadedGraph);
        assertEquals(lexCode, lexCode1);
    }

    @Test
    public void testBaseGraphCorrectness() {
        final LexCode code = CanonicalCoder.getCodeForGraph(graph);
        assertEquals(lexCode, code);
    }
    @Test
    public void testOperationA() {
        final Face setupParameterFace = graph.getFace(3);
        final Vertex setupParameterVertex = graph.getVertex(6);
        graph.operationB(setupParameterFace, setupParameterVertex);
        final LexCode preOperationACode = CanonicalCoder.getCodeForGraph(graph);

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
        final Vertex parameterVertex = graph.getVertex(6);

        graph.operationB(parameterFace, parameterVertex);
        final LexCode postOperationCode = CanonicalCoder.getCodeForGraph(graph);
        assertEquals("4 5 5 6", postOperationCode.toString().substring(0,7));

        graph.reductionB(parameterFace, parameterVertex);
        final LexCode postReductionCode = CanonicalCoder.getCodeForGraph(graph);
        assertEquals(baseGraphString, graph.toString());
        assertEquals(lexCode, postReductionCode);
    }

    @Test
    public void testOperationC() {
        final Face parameterFace = graph.getFace(3);
        final Vertex parameterVertex = graph.getVertex(2);

        graph.operationB(parameterFace, parameterVertex);
        graph.operationC(graph.getEdge(graph.getEdges().size() - 8));

        final LexCode postOperationCode = CanonicalCoder.getCodeForGraph(graph);
        assertEquals("4 5 5 5 5", postOperationCode.toString().substring(0,9));

        graph.reductionC();
        graph.reductionB(parameterFace, parameterVertex);
        final LexCode postReductionCode = CanonicalCoder.getCodeForGraph(graph);
        assertEquals(lexCode, postReductionCode);
    }

    @Test
    public void testOperationD() {
        final Face parameterFace = graph.getFace(7);
        final Vertex parameterVertex = graph.getVertex(7);

        graph.operationB(parameterFace, parameterVertex);
        graph.operationC(graph.getEdge(graph.getEdges().size() - 8));

        final LexCode preOperationECode = CanonicalCoder.getCodeForGraph(graph);

        final Edge operandEdge = graph.getEdge(28);
        graph.operationD(operandEdge);
        final LexCode newLexCode = CanonicalCoder.getCodeForFace(graph, graph.getFace(graph.getFaces().size() - 1));
        assertEquals("4 5 5 5 7", newLexCode.toString().substring(0, 9));

        graph.reductionD();
        assertEquals(preOperationECode, CanonicalCoder.getCodeForGraph(graph));
    }

    @Test
    public void testOperationF() {
        graph.operationF(graph.getFace(4), graph.getEdge(12));
        final LexCode newLexCode = CanonicalCoder.getCodeForFace(graph, graph.getFace(graph.getFaces().size() - 1));

        assertEquals("4 5 5 6 6", newLexCode.toString().substring(0, 9));
        assertEquals(6, graph.getFace(6).size());

        graph.reductionF();
        assertEquals(lexCode, CanonicalCoder.getCodeForGraph(graph));
    }
}
