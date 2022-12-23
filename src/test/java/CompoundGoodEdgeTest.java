import graph.builders.ConsistentTwoFactorBuilder;
import graph.components.Edge;
import graph.components.Graph;
import graph.components.Vertex;
import graph.preprocessors.AmbiguousVertexGetter;
import graph.preprocessors.GoodEdgeGetter;
import graph.preprocessors.VertexPolarityDistributor;
import graph.processors.Processor;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CompoundGoodEdgeTest {
    private static final Graph badGraph = new FacedTutteFragmentMock();
    private static final Graph goodGraph = new ExtendedTutteFragmentMock();
    private static final Processor processorForBadGraph = new Processor(badGraph);
    private static final Processor processorForGoodGraph = new Processor(goodGraph);

    @Test
    public void testCompoundGetGoodEdge() {
        final GoodEdgeGetter goodEdgeGetter = new GoodEdgeGetter(badGraph);
        final List<Edge> potentiallyGoodEdges = goodEdgeGetter.getResult();
        for (Edge potentialEdge : potentiallyGoodEdges) {
            final AmbiguousVertexGetter ambiguousVertexGetter = new AmbiguousVertexGetter(badGraph);
            final List<Vertex> ambiguousVertices = ambiguousVertexGetter.getResult(potentialEdge);
            final VertexPolarityDistributor distributor = new VertexPolarityDistributor(badGraph, ambiguousVertices);
            for (Graph g : distributor) {
                final ConsistentTwoFactorBuilder builder = new ConsistentTwoFactorBuilder(badGraph);
                assertTrue(builder.canBuild(potentialEdge));
            }
        }
    }

    @Test
    public void testProcessor() {
        assertFalse(processorForBadGraph.hasProperty());
        assertTrue(processorForGoodGraph.hasProperty());
    }

}
