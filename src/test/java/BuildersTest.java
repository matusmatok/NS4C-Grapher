import graph.builders.ConsistentTwoFactorBuilder;
import graph.components.Graph;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BuildersTest {
    private static final Graph graph = new ExtendedTutteFragmentMock();
    private static final ConsistentTwoFactorBuilder builder = new ConsistentTwoFactorBuilder(graph);

    private static final Graph badGraph = new TutteFragmentMock();
    private static final ConsistentTwoFactorBuilder builderForBadGraph = new ConsistentTwoFactorBuilder(badGraph);

    @Test
    public void testConsistentTwoFactorBuilder() {
        assertFalse(builder.canBuild(graph.getEdges().get(0)));
        assertTrue(builderForBadGraph.canBuild(badGraph.getEdges().get(0)));
    }
}
