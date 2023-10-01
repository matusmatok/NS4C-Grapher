import graph.components.AbstractGraph;
import graph.components.Vertex;
import graph.preprocessors.ReducibleConfigurationFinder;
import mock.FacedTutteFragmentMock;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FinderTest {
    private static final AbstractGraph graph = new FacedTutteFragmentMock();
    private static final ReducibleConfigurationFinder finder = new ReducibleConfigurationFinder(graph);
    private static final List<Vertex> ambiguousVertices = new ArrayList<>();
    static {
        ambiguousVertices.add(graph.getVertices().get(1));
        ambiguousVertices.add(graph.getVertices().get(2));
        ambiguousVertices.add(graph.getVertices().get(6));
        ambiguousVertices.add(graph.getVertices().get(11));
        ambiguousVertices.add(graph.getVertices().get(12));
        ambiguousVertices.add(graph.getVertices().get(14));
    }

    @Test
    public void testPolarityIndependentSearch() {
        assertFalse(finder.containsPolarIndependentReducibleConfig());
    }

    @Test
    @Ignore
    public void testPolarityDependentSearch() {
        finder.buildCache(new HashSet<>(ambiguousVertices));
        graph.setNegative(graph.getVertices().get(12));
        graph.setNegative(graph.getVertices().get(14));
        graph.setNegative(graph.getVertices().get(2));
        graph.setNegative(graph.getVertices().get(1));
        //assertFalse(finder.containsPolarDependentReducibleConfig());
        graph.setPositive(graph.getVertices().get(12));
        assertTrue(finder.containsPolarDependentReducibleConfig());
        graph.setPositive(graph.getVertices().get(2));
        assertTrue(finder.containsPolarDependentReducibleConfig());
    }
}
