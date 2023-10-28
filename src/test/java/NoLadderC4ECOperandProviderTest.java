import graph.canoniccoders.CachingCoder;
import graph.canoniccoders.CanonicalCoder;
import graph.components.Edge.OrientedEdge;
import graph.components.NoLadderC4ECGraph;
import graph.generators.NoLadderC4ECOperandProvider;
import graph.generators.NoLadderC4ECOperandProvider.Operand;
import java.io.FileNotFoundException;
import java.util.List;

import graph.utils.EasyList;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class NoLadderC4ECOperandProviderTest implements ResourceDependent {

    @BeforeClass
    public static void setup() {
        CanonicalCoder.noLadderExtension = true;
    }

    @AfterClass
    public static void close() {
        CanonicalCoder.noLadderExtension = false;
    }

    @Test
    public void testCaseAG() throws FileNotFoundException {
        final NoLadderC4ECGraph graph = NoLadderC4ECGraph.loadFromFile(loadResourceAsStream("gadgetA/gadgetG.txt"));
        final NoLadderC4ECOperandProvider provider = new NoLadderC4ECOperandProvider(graph, new CachingCoder(graph));

        final List<Operand> operandsA = provider.getOperandsA();
        assertEquals(4, operandsA.size());
        final List<OrientedEdge> unwrappedOperandsA = EasyList.of(
                graph.getEdge(13, 12).getOriented(graph.getVertex(13)),
                graph.getEdge(13, 9).getOriented(graph.getVertex(13)),
                graph.getEdge(14, 15).getOriented(graph.getVertex(14)),
                graph.getEdge(14, 17).getOriented(graph.getVertex(14))
        );

        for(final Operand operandA : operandsA) {
            final OrientedEdge orientedEdge = operandA.getOrientedEdge();
            assertNotNull(orientedEdge);
            assertTrue(unwrappedOperandsA.contains(orientedEdge));
        }

        final List<Operand> operandsB = provider.getOperandsB();
        assertEquals(4, operandsB.size());
        assertEquals(2, operandsB.stream().filter(operand -> operand.getFace() == graph.getFace(8, 9, 13)).count());

        final List<Operand> operandsC = provider.getOperandsC();
        assertEquals(1, operandsC.size());
        assertEquals(graph.getEdge(13, 14), operandsC.get(0).getEdge());

        final List<Operand> operandsD = provider.getOperandsD();
        assertEquals(1, operandsD.size());
        assertEquals(graph.getEdge(10, 15), operandsD.get(0).getEdge());

        final List<Operand> expectedOperandsE = List.of(
                new Operand(null, graph.getEdge(12, 16), null),
                new Operand(null, graph.getEdge(9, 10), null),
                new Operand(null, graph.getEdge(16, 17), null),
                new Operand(null, graph.getEdge(18, 19), null)
        );
        final List<Operand> operandsE = provider.getOperandsE();
        assertEquals(4, operandsE.size());

        for (final Operand operandE : operandsE) {
            assertTrue(expectedOperandsE.contains(operandE));
        }

        final List<Operand> expectedOperandsF = List.of(
                new Operand(graph.getFace(13, 9, 10), graph.getEdge(13, 14), null),
                new Operand(graph.getFace(12, 13, 14), graph.getEdge(13, 14), null),
                new Operand(graph.getFace(10, 11, 19), graph.getEdge(10, 15), null),
                new Operand(graph.getFace(10, 11, 19), graph.getEdge(18, 19), null)
        );
        final List<Operand> operandsF = provider.getOperandsF();
        assertEquals(4, operandsF.size());

        for (final Operand operandF : operandsF) {
            assertTrue(expectedOperandsF.contains(operandF));
        }

        final List<Operand> operandsG = provider.getOperandsG();
        assertEquals(1, operandsG.size());
        assertEquals(graph.getEdge(10, 11), operandsG.get(0).getEdge());

        assertTrue(provider.getOperandsH().isEmpty());
    }

    @Test
    public void testCaseAGv2() throws FileNotFoundException {
        final NoLadderC4ECGraph graph = NoLadderC4ECGraph.loadFromFile(loadResourceAsStream("gadgetA/gadgetGv2.txt"));
        final NoLadderC4ECOperandProvider provider = new NoLadderC4ECOperandProvider(graph, new CachingCoder(graph));

        final List<Operand> operandsB = provider.getOperandsB();
        assertEquals(6, operandsB.size());

        final List<Operand> operandsF = provider.getOperandsF();
        final List<Operand> expectedOperandsF = List.of(
                new Operand(graph.getFace(13, 9, 10), graph.getEdge(13, 14), null),
                new Operand(graph.getFace(12, 13, 14), graph.getEdge(13, 14), null),
                new Operand(graph.getFace(10, 11, 19), graph.getEdge(18, 19), null)
        );
        for (final Operand operandF : operandsF) {
            assertTrue(expectedOperandsF.contains(operandF));
        }
    }
}
