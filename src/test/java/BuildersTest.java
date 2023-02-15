import graph.builders.UnavoidableBuilder;
import graph.components.Graph;
import mock.ExtendedTutteFragmentMock;
import mock.TutteFragmentMock;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BuildersTest {
    private static final Graph graph = new ExtendedTutteFragmentMock();
    private static final UnavoidableBuilder builder = new UnavoidableBuilder(graph);

    private static final Graph badGraph = new TutteFragmentMock();
    private static final UnavoidableBuilder builderForBadGraph = new UnavoidableBuilder(badGraph);

    @Test
    public void testConsistentTwoFactorBuilder() {
        assertFalse(builder.canBuild(graph.getEdges().get(0)));
        assertTrue(builderForBadGraph.canBuild(badGraph.getEdges().get(0)));
    }
}
