import graph.builders.UnavoidableBuilder;
import graph.components.Edge;
import graph.components.AbstractGraph;
import graph.components.NoLadderC4ECGraph;
import graph.components.Vertex;
import graph.preprocessors.HamUnavoidableEdgeGetter;
import graph.preprocessors.UnavoidableAmbiguousVertexGetter;
import graph.preprocessors.VertexPolarityDistributor;
import graph.processors.ham.HamiltonianProcessor;
import graph.processors.ProcessorTypeDEF;
import graph.processors.UnavoidableEdgeProcessor;
import graph.processors.UntouchableEdgeProcessor;
import mock.ExtendedTenTuttovTotoMock;
import mock.ExtendedTutteFragmentMock;
import mock.FacedTutteFragmentMock;
import mock.TenTuttovTotoMock;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ProcessorsTest implements ResourceDependent {
    private static final AbstractGraph badGraph = new FacedTutteFragmentMock();
    private static final AbstractGraph goodGraph = new ExtendedTutteFragmentMock();

    private static final AbstractGraph tenTuttovToto = new TenTuttovTotoMock();

    private static final AbstractGraph extendedTenTuttovToto = new ExtendedTenTuttovTotoMock();
    private static final UnavoidableEdgeProcessor UNAVOIDABLE_EDGE_PROCESSOR_FOR_BAD_GRAPH = new UnavoidableEdgeProcessor(badGraph);
    private static final UnavoidableEdgeProcessor UNAVOIDABLE_EDGE_PROCESSOR_FOR_GOOD_GRAPH = new UnavoidableEdgeProcessor(goodGraph);
    private static final UntouchableEdgeProcessor UNTOUCHABLE_EDGE_PROCESSOR_FOR_BAD_GRAPH = new UntouchableEdgeProcessor(tenTuttovToto);
    private static final UntouchableEdgeProcessor UNTOUCHABLE_EDGE_PROCESSOR_FOR_GOOD_GRAPH = new UntouchableEdgeProcessor(extendedTenTuttovToto);

    @Test
    public void testCompoundGetGoodEdge() {
        final HamUnavoidableEdgeGetter hamUnavoidableEdgeGetter = new HamUnavoidableEdgeGetter(badGraph);
        final List<Edge> potentiallyGoodEdges = hamUnavoidableEdgeGetter.getResult();
        for (Edge potentialEdge : potentiallyGoodEdges) {
            final UnavoidableAmbiguousVertexGetter ambiguousVertexGetter = new UnavoidableAmbiguousVertexGetter(badGraph);
            final List<Vertex> ambiguousVertices = ambiguousVertexGetter.getResult(potentialEdge);
            final VertexPolarityDistributor distributor = new VertexPolarityDistributor(badGraph, ambiguousVertices);
            for (AbstractGraph g : distributor) {
                final UnavoidableBuilder builder = new UnavoidableBuilder(badGraph);
                assertTrue(builder.canBuild(potentialEdge));
            }
        }
    }

    @Test
    public void testUnavoidableProcessors() {
        assertFalse(UNAVOIDABLE_EDGE_PROCESSOR_FOR_BAD_GRAPH.hasProperty());
        assertTrue(UNAVOIDABLE_EDGE_PROCESSOR_FOR_GOOD_GRAPH.hasProperty());
    }

    @Test
    public void testUntouchableProcessors() {
        assertFalse(UNTOUCHABLE_EDGE_PROCESSOR_FOR_BAD_GRAPH.hasProperty());
        assertTrue(UNTOUCHABLE_EDGE_PROCESSOR_FOR_GOOD_GRAPH.hasProperty());
    }

    @Test
    public void testProcessorTypeDEF() {
        final ProcessorTypeDEF processorForBadGraph = new ProcessorTypeDEF(badGraph);
        final ProcessorTypeDEF processorForGoodGraph = new ProcessorTypeDEF(goodGraph);
        assertFalse(processorForBadGraph.findGoodEdges());
        assertTrue(processorForGoodGraph.findGoodEdges());
    }

    @Test
    public void testHamiltonianProcessor() throws FileNotFoundException {
        final NoLadderC4ECGraph nonHamiltonian = NoLadderC4ECGraph.loadFromFile(loadResourceAsStream("nonHamiltonianGraph.txt"));
        final HamiltonianProcessor processorN = new HamiltonianProcessor(nonHamiltonian);
        assertFalse(processorN.hasProperty());

        final NoLadderC4ECGraph hamiltonian = new NoLadderC4ECGraph();
        hamiltonian.init();
        final HamiltonianProcessor processorH = new HamiltonianProcessor(hamiltonian);
        assertTrue(processorH.hasProperty());
    }

}
