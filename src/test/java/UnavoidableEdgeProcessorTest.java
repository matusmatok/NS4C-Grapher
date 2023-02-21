import graph.builders.ConsistentTwoFactorBuilder;
import graph.components.Edge;
import graph.components.Graph;
import graph.components.Vertex;
import graph.preprocessors.AmbiguousVertexGetter;
import graph.preprocessors.HamUnavoidableEdgeGetter;
import graph.preprocessors.VertexPolarityDistributor;
import graph.processors.UnavoidableEdgeProcessor;
import mock.ExtendedTutteFragmentMock;
import mock.FacedTutteFragmentMock;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UnavoidableEdgeProcessorTest {
    private static final Graph badGraph = new FacedTutteFragmentMock();
    private static final Graph goodGraph = new ExtendedTutteFragmentMock();
    private static final UnavoidableEdgeProcessor UNAVOIDABLE_EDGE_PROCESSOR_FOR_BAD_GRAPH = new UnavoidableEdgeProcessor(badGraph);
    private static final UnavoidableEdgeProcessor UNAVOIDABLE_EDGE_PROCESSOR_FOR_GOOD_GRAPH = new UnavoidableEdgeProcessor(goodGraph);

    @Test
    public void testCompoundGetGoodEdge() {
        final HamUnavoidableEdgeGetter hamUnavoidableEdgeGetter = new HamUnavoidableEdgeGetter(badGraph);
        final List<Edge> potentiallyGoodEdges = hamUnavoidableEdgeGetter.getResult();
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
        assertFalse(UNAVOIDABLE_EDGE_PROCESSOR_FOR_BAD_GRAPH.hasProperty());
        assertTrue(UNAVOIDABLE_EDGE_PROCESSOR_FOR_GOOD_GRAPH.hasProperty());
    }

}
