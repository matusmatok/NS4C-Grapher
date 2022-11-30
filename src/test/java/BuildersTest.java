import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import graph.builders.ConsistentTwoFactorBuilder;
import graph.components.Graph;
import org.junit.Test;

public class BuildersTest {
    private static final Graph graph = new ExtendedTutteFragmentMock();
    private static final ConsistentTwoFactorBuilder builder = new ConsistentTwoFactorBuilder(graph, graph.getEdges().get(0));

    private static final Graph badGraph = new TutteFragmentMock();
    private static final ConsistentTwoFactorBuilder builderForBadGraph = new ConsistentTwoFactorBuilder(badGraph, badGraph.getEdges().get(0));

    @Test
    public void testConsistentTwoFactorBuilder() {
        assertFalse(builder.canBuild());
        assertTrue(builderForBadGraph.canBuild());
    }
}
