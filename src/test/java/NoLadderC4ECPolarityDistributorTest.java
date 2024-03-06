import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import graph.builders.UnavoidableBuilder;
import graph.components.AbstractGraph;
import graph.components.Vertex;
import graph.preprocessors.NoLadderC4ECPolarityDistributor;
import graph.utils.SubsetIterator;
import mock.FacedTutteFragmentMock;
import org.junit.Test;

import java.util.List;

public class NoLadderC4ECPolarityDistributorTest {

    @Test
    public void testSubsetIterator() {
        final SubsetIterator iterator = new SubsetIterator(6, 3);
        int sum = 0;
        for (SubsetIterator it = iterator; it.hasNext(); ) {
            it.next();
            sum++;
        }
        assertEquals(20, sum);

        final SubsetIterator iterator2 = new SubsetIterator(20, 4);
        int sum2 = 0;
        for (SubsetIterator it = iterator2; it.hasNext(); ) {
            it.next();
            sum2++;
        }
        assertEquals(4845, sum2);
    }

    @Test
    public void testDistributor() {
        final AbstractGraph testGraph = new FacedTutteFragmentMock();
        final List<Vertex> ambiguousVertices = List.of(
                testGraph.getVertex(1),
                testGraph.getVertex(2),
                testGraph.getVertex(6),
                testGraph.getVertex(11),
                testGraph.getVertex(12),
                testGraph.getVertex(14)
        );
        final NoLadderC4ECPolarityDistributor distributor = new NoLadderC4ECPolarityDistributor(testGraph, ambiguousVertices, 28);
        final UnavoidableBuilder builder = new UnavoidableBuilder(testGraph);

        int counter = 0;
        for (final AbstractGraph graph : distributor) {
            if (!builder.canBuild(graph.getEdge(0))) {
                graph.print();
                counter++;
            }
        }

        assertEquals(32, counter);

        counter = 0;
        for (final AbstractGraph graph : distributor) {
            counter++;
        }
        assertEquals(2968, counter);
    }
}
