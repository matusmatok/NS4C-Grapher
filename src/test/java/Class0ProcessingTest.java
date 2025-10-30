import graph.builders.Class0Builder;
import graph.components.NoLadderC4ECGraph;
import graph.components.Vertex;
import graph.preprocessors.ambiguous.Class0AmbiguousVertexGetter;
import graph.preprocessors.distributors.NoLadderC4ECPolarityDistributor;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Class0ProcessingTest implements ResourceDependent {

    @Test
    public void testAmbiguousVertexGetter() throws FileNotFoundException {
        final NoLadderC4ECGraph graph = NoLadderC4ECGraph.loadFromFile(loadResourceAsStream("smallestCounterExample.txt"));
        final Class0AmbiguousVertexGetter getter = new Class0AmbiguousVertexGetter(graph);

        final List<Vertex> ambiguousVertices = getter.getResult(graph.getEdge(0));

        System.out.println(ambiguousVertices);
        final List<Vertex> expectedAmbiguousVertices = List.of(graph.getVertex(9), graph.getVertex(19),
                graph.getVertex(25), graph.getVertex(35));
        assertTrue(ambiguousVertices.containsAll(expectedAmbiguousVertices));
    }

    @Test
    public void testBuilder() throws FileNotFoundException {
        final NoLadderC4ECGraph graph = NoLadderC4ECGraph.loadFromFile(loadResourceAsStream("smallestCounterExample.txt"));
        for (Vertex v : graph.getVertices()) {
            v.setSelfPositive();
        }

        final Class0Builder builder = new Class0Builder(graph);

        assertTrue(builder.canBuild(graph.getEdge(0)));

        graph.getVertex(9).setSelfNegative();
        graph.getVertex(19).setSelfNegative();
        graph.getVertex(25).setSelfNegative();
        graph.getVertex(35).setSelfNegative();

        graph.getVertex(2).setSelfPseudoNegative();
        graph.getVertex(5).setSelfPseudoNegative();
        graph.getVertex(12).setSelfPseudoNegative();
        graph.getVertex(17).setSelfPseudoNegative();
        graph.getVertex(28).setSelfPseudoNegative();
        graph.getVertex(33).setSelfPseudoNegative();

        assertFalse(builder.canBuild(graph.getEdge(0)));
    }

    @Test
    public void testDistributor() throws FileNotFoundException {
        final NoLadderC4ECGraph graph = NoLadderC4ECGraph.loadFromFile(loadResourceAsStream("smallestCounterExample.txt"));
        final NoLadderC4ECPolarityDistributor.NoLadderDistributorFactory factory =
                new NoLadderC4ECPolarityDistributor.NoLadderDistributorFactory(62);
        final Class0AmbiguousVertexGetter getter = new Class0AmbiguousVertexGetter(graph);

        final List<Vertex> ambiguousVertices = getter.getResult(graph.getEdge(0));
        final NoLadderC4ECPolarityDistributor distributor = (NoLadderC4ECPolarityDistributor) factory.produceDistributor(graph, ambiguousVertices);

        final String expectedPolarity = "PPCPPCPPPNPPCPPPPCPNPPPPPNPPCPPPPCPNPP";

        long counter = 0;

        for (var g : distributor) {
            counter++;
            final StringBuilder sb = new StringBuilder();
            for (final Vertex v : g.getVertices()) {
                if (v.getType() == Vertex.Type.POSITIVE) sb.append("P");
                if (v.getType() == Vertex.Type.NEGATIVE) sb.append("N");
                if (v.getType() == Vertex.Type.CUBE) sb.append("C");
            }
            if (sb.toString().equals(expectedPolarity)) {
                Assert.fail();
            }
        }
        System.out.println(counter);
    }
}
