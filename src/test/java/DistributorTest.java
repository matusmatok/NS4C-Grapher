import static org.junit.Assert.assertFalse;

import graph.components.Graph;
import graph.components.Vertex;
import graph.preprocessors.VertexPolarityDistributor;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DistributorTest {
    final static Graph graph = new TutteFragmentMock();
    final static List<Vertex> ambiguousVertices = new ArrayList<>();
    static {
        for (int i =0; i <5; i++) ambiguousVertices.add(graph.getVertices().get(i));
    }
    final static VertexPolarityDistributor distributor = new VertexPolarityDistributor(graph, ambiguousVertices);

    @Test
    public void testDistribution() {
        final Set<String> resultSet = new HashSet<>();
        for (Graph g: distributor) {
            final StringBuilder sb = new StringBuilder();
            for (int i=0; i<5; i++) sb.append(g.getVertices().get(i).getWeight());
            final String string = sb.toString();
            assertFalse(resultSet.contains(string));
            resultSet.add(string);
        }

    }
}
