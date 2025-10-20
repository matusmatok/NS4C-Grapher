import graph.components.AbstractGraph;
import graph.components.Edge;
import graph.components.EdgePair;
import graph.components.NoLadderC4ECGraph;
import graph.preprocessors.HamUntouchablePairGetter;
import mock.FacedTutteFragmentMock;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class EdgePairProcessingTest implements ResourceDependent{

    @Test
    public void testSetContainment() {
        final Set<EdgePair> set = new HashSet<>();
        final AbstractGraph graph = new FacedTutteFragmentMock();

        final Edge e1 = graph.getEdge(4);
        final Edge e2 = graph.getEdge(5);
        final Edge e3 = graph.getEdge(9);
        final Edge e4 = graph.getEdge(8);

        set.add(new EdgePair(e1, e2));

        final EdgePair ep1a = new EdgePair(e1, e2);
        assertTrue(set.contains(ep1a));

        final EdgePair ep1b = new EdgePair(e2, e1);
        assertTrue(set.contains(ep1b));

        final EdgePair ep23 = new EdgePair(e2, e3);
        assertFalse(set.contains(ep23));

        final EdgePair ep34 = new EdgePair(e3, e4);
        assertFalse(set.contains(ep34));
    }

    @Test
    public void testHamPairGetter() throws FileNotFoundException {
        final NoLadderC4ECGraph graph = NoLadderC4ECGraph.loadFromFile(loadResourceAsStream("5prism.txt"));
        final HamUntouchablePairGetter getter = new HamUntouchablePairGetter(graph);

        final List<Edge> rungs = List.of(graph.getEdge(1), graph.getEdge(4), graph.getEdge(6), graph.getEdge(8), graph.getEdge(9));
        final List<EdgePair> goodPairs = getter.getResult();
        final Set<EdgePair> expectedSet = Set.of(
                new EdgePair(graph.getEdge(1), graph.getEdge(6)),
                new EdgePair(graph.getEdge(1), graph.getEdge(8)),
                new EdgePair(graph.getEdge(4), graph.getEdge(8)),
                new EdgePair(graph.getEdge(4), graph.getEdge(9)),
                new EdgePair(graph.getEdge(6), graph.getEdge(9))
        );

        assertFalse(goodPairs.isEmpty());
        assertEquals(expectedSet, new HashSet<>(goodPairs));
    }
}
