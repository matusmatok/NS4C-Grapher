import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import graph.components.Edge;
import graph.components.Graph;
import graph.components.Vertex;
import graph.processors.AbstractPreProcessor;
import java.util.List;

import graph.processors.AmbiguousVertexGetter;
import graph.processors.GoodEdgeGetter;
import org.junit.Test;

public class PreProcessorTest {

    private static final Graph graph = new TutteFragmentMock();
    private static final AbstractPreProcessor goodEdgeGetter = new GoodEdgeGetter(graph);

    @Test
    public void testGetGoodEdges() {
        List<Edge> result = goodEdgeGetter.getResult();
        assertTrue(result.contains(graph.getEdges().get(0)));
        assertTrue(result.contains(graph.getEdges().get(4)));
        assertTrue(result.contains(graph.getEdges().get(17)));
        assertFalse(result.contains(graph.getEdges().get(1)));
    }

    @Test
    public void testGetAmbiguousVertices() {
        final AbstractPreProcessor ambiguousVertexGetter = new AmbiguousVertexGetter(graph, graph.getEdges().get(0));
        List<Vertex> result = ambiguousVertexGetter.getResult();
        assertTrue(result.contains((graph.getVertices().get(1))));
        assertTrue(result.contains((graph.getVertices().get(2))));
        assertTrue(result.contains((graph.getVertices().get(6))));
        assertTrue(result.contains((graph.getVertices().get(11))));
        assertTrue(result.contains((graph.getVertices().get(12))));
        assertTrue(result.contains((graph.getVertices().get(14))));
        assertFalse(result.contains(graph.getVertices().get(0)));
        assertFalse(result.contains(graph.getVertices().get(5)));
        assertFalse(result.contains(graph.getVertices().get(4)));
        assertFalse(result.contains(graph.getVertices().get(15)));
        assertFalse(result.contains(graph.getVertices().get(10)));
        assertFalse(result.contains(graph.getVertices().get(7)));
        assertFalse(result.contains(graph.getVertices().get(3)));

    }
}
