import graph.components.Edge;
import graph.components.AbstractGraph;
import graph.components.NoLadderC4ECGraph;
import graph.components.Vertex;
import graph.preprocessors.HamUnavoidableEdgeGetter;
import graph.preprocessors.HamUntouchableEdgeGetter;
import graph.preprocessors.ambiguous.UnavoidableAmbiguousVertexGetter;
import graph.preprocessors.ambiguous.UntouchableAmbiguousVertexGetter;
import graph.preprocessors.util.VertexSetOmittingHamUtil;
import mock.ExtendedTenTuttovTotoMock;
import mock.TutteFragmentMock;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PreProcessorTest implements ResourceDependent{

    private static final AbstractGraph TUTTE_FRAGMENT_MOCK = new TutteFragmentMock();
    private static final AbstractGraph EXTENDED_TEN_TUTTOV_TOTO_MOCK = new ExtendedTenTuttovTotoMock();
    private static final HamUnavoidableEdgeGetter UNAVOIDABLE_EDGE_GETTER = new HamUnavoidableEdgeGetter(TUTTE_FRAGMENT_MOCK);
    private static final HamUntouchableEdgeGetter UNTOUCHABLE_EDGE_GETTER = new HamUntouchableEdgeGetter(EXTENDED_TEN_TUTTOV_TOTO_MOCK);

    @Test
    public void testGetUnavoidableEdges() {
        final List<Edge> result = UNAVOIDABLE_EDGE_GETTER.getResult();
        assertTrue(result.contains(TUTTE_FRAGMENT_MOCK.getEdges().get(0)));
        assertTrue(result.contains(TUTTE_FRAGMENT_MOCK.getEdges().get(4)));
        assertTrue(result.contains(TUTTE_FRAGMENT_MOCK.getEdges().get(17)));
        assertFalse(result.contains(TUTTE_FRAGMENT_MOCK.getEdges().get(1)));
    }

    @Test
    public void testGetUntouchableEdges() {
        final List<Edge> result = UNTOUCHABLE_EDGE_GETTER.getResult();
        assertTrue(result.contains(EXTENDED_TEN_TUTTOV_TOTO_MOCK.getEdges().get(48)));
        assertTrue(result.contains(EXTENDED_TEN_TUTTOV_TOTO_MOCK.getEdges().get(51)));
        assertFalse(result.contains(EXTENDED_TEN_TUTTOV_TOTO_MOCK.getEdges().get(24)));
        assertFalse(result.contains(EXTENDED_TEN_TUTTOV_TOTO_MOCK.getEdges().get(0)));
        assertFalse(result.contains(EXTENDED_TEN_TUTTOV_TOTO_MOCK.getEdges().get(31)));
        assertFalse(result.contains(EXTENDED_TEN_TUTTOV_TOTO_MOCK.getEdges().get(12)));
    }

    @Test
    public void testGetAmbiguousVertices() {
        final UnavoidableAmbiguousVertexGetter ambiguousVertexGetter = new UnavoidableAmbiguousVertexGetter(TUTTE_FRAGMENT_MOCK);
        final List<Vertex> result = ambiguousVertexGetter.getResult(TUTTE_FRAGMENT_MOCK.getEdges().get(0));
        assertTrue(result.contains((TUTTE_FRAGMENT_MOCK.getVertices().get(1))));
        assertTrue(result.contains((TUTTE_FRAGMENT_MOCK.getVertices().get(2))));
        assertTrue(result.contains((TUTTE_FRAGMENT_MOCK.getVertices().get(6))));
        assertTrue(result.contains((TUTTE_FRAGMENT_MOCK.getVertices().get(11))));
        assertTrue(result.contains((TUTTE_FRAGMENT_MOCK.getVertices().get(12))));
        assertTrue(result.contains((TUTTE_FRAGMENT_MOCK.getVertices().get(14))));
        assertFalse(result.contains(TUTTE_FRAGMENT_MOCK.getVertices().get(0)));
        assertFalse(result.contains(TUTTE_FRAGMENT_MOCK.getVertices().get(5)));
        assertFalse(result.contains(TUTTE_FRAGMENT_MOCK.getVertices().get(4)));
        assertFalse(result.contains(TUTTE_FRAGMENT_MOCK.getVertices().get(15)));
        assertFalse(result.contains(TUTTE_FRAGMENT_MOCK.getVertices().get(10)));
        assertFalse(result.contains(TUTTE_FRAGMENT_MOCK.getVertices().get(7)));
        assertFalse(result.contains(TUTTE_FRAGMENT_MOCK.getVertices().get(3)));

        final UntouchableAmbiguousVertexGetter untouchableAmbiguousVertexGetter = new UntouchableAmbiguousVertexGetter(EXTENDED_TEN_TUTTOV_TOTO_MOCK);
        final List<Vertex> untouchableResult = untouchableAmbiguousVertexGetter.getResult(EXTENDED_TEN_TUTTOV_TOTO_MOCK.getEdges().get(48));
        assertEquals(37, untouchableResult.size());
    }

    @Test
    public void testVertexSetOmittingHamUtil() throws FileNotFoundException {
        assertFalse(VertexSetOmittingHamUtil.isUnavoidConstructable(TUTTE_FRAGMENT_MOCK, Set.of(TUTTE_FRAGMENT_MOCK.getVertex(1)), TUTTE_FRAGMENT_MOCK.getEdge(0)));
        assertFalse(VertexSetOmittingHamUtil.isUnavoidConstructable(TUTTE_FRAGMENT_MOCK, Set.of(TUTTE_FRAGMENT_MOCK.getVertex(6)), TUTTE_FRAGMENT_MOCK.getEdge(0)));
        assertTrue(VertexSetOmittingHamUtil.isUnavoidConstructable(TUTTE_FRAGMENT_MOCK, Set.of(TUTTE_FRAGMENT_MOCK.getVertex(0)), TUTTE_FRAGMENT_MOCK.getEdge(0)));

        final AbstractGraph nonHamiltonian = NoLadderC4ECGraph.loadFromFile(loadResourceAsStream("nonHamiltonianGraph.txt"));
        assertTrue(VertexSetOmittingHamUtil.isUnavoidConstructable(nonHamiltonian,
                Set.of(nonHamiltonian.getVertex(10),
                        nonHamiltonian.getVertex(41),
                        nonHamiltonian.getVertex(0),
                        nonHamiltonian.getVertex(31)), nonHamiltonian.getEdge(5, 36)));

        assertFalse(VertexSetOmittingHamUtil.isUnavoidConstructable(nonHamiltonian,
                Set.of(nonHamiltonian.getVertex(10),
                        nonHamiltonian.getVertex(41),
                        nonHamiltonian.getVertex(0),
                        nonHamiltonian.getVertex(31)), nonHamiltonian.getEdge(8,9)));

        assertTrue(VertexSetOmittingHamUtil.isUntouchConstructable(nonHamiltonian,
                Set.of(nonHamiltonian.getVertex(10),
                        nonHamiltonian.getVertex(41),
                        nonHamiltonian.getVertex(0),
                        nonHamiltonian.getVertex(31)), nonHamiltonian.getEdge(8,9)));
    }
}
