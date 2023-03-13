import graph.builders.UnavoidableBuilder;
import graph.builders.UntouchableBuilder;
import graph.components.Graph;
import mock.ExtendedTenTuttovTotoMock;
import mock.ExtendedTutteFragmentMock;
import mock.ObscureTutteFragmentMock;
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

    private static final Graph obscureTutteFragment = new ObscureTutteFragmentMock();
    private static final UnavoidableBuilder obscureBuilder = new UnavoidableBuilder(obscureTutteFragment);

    @Test
    public void testConsistentTwoFactorBuilder() {
        assertFalse(builder.canBuild(graph.getEdges().get(0)));
        assertTrue(builderForBadGraph.canBuild(badGraph.getEdges().get(0)));
        assertFalse(untouchableBuilder.canBuild(tenTuttovTotoExtended.getEdges().get(48)));
    }

    @Test
    public void testObsucreFragmentCycles() {
        assertTrue(obscureBuilder.canBuild(obscureTutteFragment.getEdges().get(0)));
    }
}
