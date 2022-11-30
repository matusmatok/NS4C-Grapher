import static org.junit.Assert.assertTrue;

import graph.builders.ConsistentTwoFactorBuilder;
import graph.components.Edge;
import graph.components.Graph;
import graph.components.Vertex;
import graph.preprocessors.AmbiguousVertexGetter;
import graph.preprocessors.GoodEdgeGetter;
import graph.preprocessors.VertexPolarityDistributor;
import java.util.List;
import org.junit.Test;

public class CompoundGoodEdgeTest {
    private static final Graph badGraph = new TutteFragmentMock();

    @Test
    public void testCompoundGetGoodEdge() {
        final GoodEdgeGetter goodEdgeGetter = new GoodEdgeGetter(badGraph);
        final List<Edge> potentiallyGoodEdges = goodEdgeGetter.getResult();
        for (Edge potentialEdge : potentiallyGoodEdges) {
            final AmbiguousVertexGetter ambiguousVertexGetter = new AmbiguousVertexGetter(badGraph, potentialEdge);
            final List<Vertex> ambiguousVertices = ambiguousVertexGetter.getResult();
            final VertexPolarityDistributor distributor = new VertexPolarityDistributor(badGraph, ambiguousVertices);
            for (Graph g : distributor) {
                final ConsistentTwoFactorBuilder builder = new ConsistentTwoFactorBuilder(badGraph, potentialEdge);
                assertTrue(builder.canBuild());
            }
        }
    }

}
