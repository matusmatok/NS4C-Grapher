import graph.components.Edge;
import graph.components.Graph;
import graph.components.Vertex;
import graph.preprocessors.HamUnavoidableEdgeGetter;
import graph.preprocessors.HamUntouchableEdgeGetter;
import graph.preprocessors.UnavoidableAmbiguousVertexGetter;
import graph.preprocessors.UntouchableAmbiguousVertexGetter;
import mock.ExtendedTenTuttovTotoMock;
import mock.ExtendedTutteFragmentMock;
import mock.TenTuttovTotoMock;
import mock.TutteFragmentMock;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PreProcessorTest {

    private static final Graph TUTTE_FRAGMENT_MOCK = new TutteFragmentMock();
    private static final Graph TEN_TUTTOV_TOTO_MOCK = new TenTuttovTotoMock();
    private static final Graph EXTENDED_TEN_TUTTOV_TOTO_MOCK = new ExtendedTenTuttovTotoMock();
    private static final HamUnavoidableEdgeGetter UNAVOIDABLE_EDGE_GETTER = new HamUnavoidableEdgeGetter(TUTTE_FRAGMENT_MOCK);
    private static final HamUntouchableEdgeGetter UNTOUCHABLE_EDGE_GETTER = new HamUntouchableEdgeGetter(TEN_TUTTOV_TOTO_MOCK);

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
        assertTrue(result.contains(TEN_TUTTOV_TOTO_MOCK.getEdges().get(48)));
        assertTrue(result.contains(TEN_TUTTOV_TOTO_MOCK.getEdges().get(51)));
        assertFalse(result.contains(TEN_TUTTOV_TOTO_MOCK.getEdges().get(24)));
        assertFalse(result.contains(TEN_TUTTOV_TOTO_MOCK.getEdges().get(0)));
        assertFalse(result.contains(TEN_TUTTOV_TOTO_MOCK.getEdges().get(31)));
        assertFalse(result.contains(TEN_TUTTOV_TOTO_MOCK.getEdges().get(12)));
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
        assertEquals(48, untouchableResult.size());
    }
}
