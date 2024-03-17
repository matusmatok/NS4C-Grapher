import static org.junit.Assert.assertEquals;

import graph.components.AbstractGraph;
import graph.components.Edge;
import graph.components.Vertex;
import graph.preprocessors.distributors.negset.NegativeSetsDistributor;
import graph.preprocessors.distributors.negset.UnavoidableNegativeSetDistributor;
import graph.utils.EasyList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import mock.FacedTutteFragmentMock;
import org.junit.Test;

public class NegativeSetDistributorTest {
    private static final AbstractGraph graph = new FacedTutteFragmentMock();

    @Test
    public void testNegativeSetDistributor() {
        final List<Vertex> ambiguousVertices = EasyList.of(
                graph.getVertex(1),
                graph.getVertex(2),
                graph.getVertex(6),
                graph.getVertex(11),
                graph.getVertex(12),
                graph.getVertex(14));
        final Edge potentialEdge = graph.getEdge(0, 15);
        final List<Vertex> cubes = EasyList.of(graph.getVertex(4));

        final List<Vertex> ambigCopy = new ArrayList<>(ambiguousVertices);
        final NegativeSetsDistributor distributor = new UnavoidableNegativeSetDistributor(graph, potentialEdge, cubes);

        int counter = 0;
        final Iterator<AbstractGraph> graphIterator = distributor.iterator(ambigCopy);
        assertEquals(2, ambigCopy.size());

        while (graphIterator.hasNext()) {
            counter++;
            graphIterator.next();
        }
        assertEquals(2, counter);

        final List<Vertex> cubes2 = EasyList.of();
        final List<Vertex> ambigCopy2 = new ArrayList<>(ambiguousVertices);
        final NegativeSetsDistributor distributor2 = new UnavoidableNegativeSetDistributor(graph, potentialEdge, cubes2);

        counter = 0;
        final Iterator<AbstractGraph> graphIterator2 = distributor2.iterator(ambigCopy2);
        assertEquals(2, ambigCopy2.size());

        while (graphIterator.hasNext()) {
            counter++;
            graphIterator.next();
        }
        assertEquals(0, counter);
    }
}
