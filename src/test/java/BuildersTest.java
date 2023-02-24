import graph.builders.UnavoidableBuilder;
import graph.builders.UntouchableBuilder;
import graph.components.Graph;
import mock.ExtendedTenTuttovTotoMock;
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

    private static final Graph tenTuttovTotoExtended = new ExtendedTenTuttovTotoMock();
    private static final UntouchableBuilder untouchableBuilder = new UntouchableBuilder(tenTuttovTotoExtended);

    @Test
    public void testConsistentTwoFactorBuilder() {
        assertFalse(builder.canBuild(graph.getEdges().get(0)));
        assertTrue(builderForBadGraph.canBuild(badGraph.getEdges().get(0)));
        assertFalse(untouchableBuilder.canBuild(tenTuttovTotoExtended.getEdges().get(48)));
    }
}
