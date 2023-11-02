import graph.canoniccoders.CachingCoder;
import graph.canoniccoders.CanonicalCoder;
import graph.components.Edge.OrientedEdge;
import graph.components.Face;
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
    public void testCaseAAv1() throws FileNotFoundException {
        final NoLadderC4ECGraph graph = NoLadderC4ECGraph.loadFromFile(loadResourceAsStream("gadgetA/gadgetAv1.txt"));
        final NoLadderC4ECOperandProvider provider = new NoLadderC4ECOperandProvider(graph, new CachingCoder(graph));

        final List<Operand> operandsA = provider.getOperandsA();
        assertEquals(4, operandsA.size()); //4 because symmetries

        final List<Operand> operandsB = provider.getOperandsB();
        assertEquals(2, operandsB.size());
    }

    @Test
    public void testCaseAAv2() throws FileNotFoundException {
        final NoLadderC4ECGraph graph = NoLadderC4ECGraph.loadFromFile(loadResourceAsStream("gadgetA/gadgetAv2.txt"));
        final NoLadderC4ECOperandProvider provider = new NoLadderC4ECOperandProvider(graph, new CachingCoder(graph));

        final List<Operand> operandsA = provider.getOperandsA();
        assertEquals(4, operandsA.size()); //4 because symmetries

        final List<Operand> operandsB = provider.getOperandsB();
        assertTrue(operandsB.isEmpty());
    }

    @Test
    public void testCaseAB() throws FileNotFoundException {
        final NoLadderC4ECGraph graph = NoLadderC4ECGraph.loadFromFile(loadResourceAsStream("gadgetA/gadgetB.txt"));
        final NoLadderC4ECOperandProvider provider = new NoLadderC4ECOperandProvider(graph, new CachingCoder(graph));

        final List<Operand> operandsA = provider.getOperandsA();
        assertEquals(8, operandsA.size());
        assertTrue(operandsA.contains(new Operand(null, null, graph.getEdge(32, 29).getOriented(graph.getVertex(32)))));
        assertTrue(operandsA.contains(new Operand(null, null, graph.getEdge(32, 31).getOriented(graph.getVertex(32)))));
        assertTrue(operandsA.contains(new Operand(null, null, graph.getEdge(33, 34).getOriented(graph.getVertex(33)))));
        assertTrue(operandsA.contains(new Operand(null, null, graph.getEdge(33, 36).getOriented(graph.getVertex(33)))));

        assertTrue(provider.getOperandsC().isEmpty());
    }

    @Test
    public void testCaseAC() throws FileNotFoundException {
        final NoLadderC4ECGraph graph = NoLadderC4ECGraph.loadFromFile(loadResourceAsStream("gadgetA/gadgetC.txt"));
        final NoLadderC4ECOperandProvider provider = new NoLadderC4ECOperandProvider(graph, new CachingCoder(graph));

        assertEquals(4, provider.getOperandsA().size());
        assertEquals(4, provider.getOperandsB().size());
        assertEquals(1, provider.getOperandsC().size());
        assertSame(provider.getOperandsC().get(0).getEdge(), graph.getEdge(13, 14));

        assertTrue(provider.getOperandsD().isEmpty());
    }

    @Test
    public void testCaseAD() throws FileNotFoundException {
        final NoLadderC4ECGraph graph = NoLadderC4ECGraph.loadFromFile(loadResourceAsStream("gadgetA/gadgetD.txt"));
        final NoLadderC4ECOperandProvider provider = new NoLadderC4ECOperandProvider(graph, new CachingCoder(graph));

        assertEquals(4, provider.getOperandsA().size());
        assertEquals(4, provider.getOperandsB().size());
        assertEquals(1, provider.getOperandsC().size());
        assertEquals(1, provider.getOperandsD().size());
        assertSame(graph.getEdge(10, 15), provider.getOperandsD().get(0).getEdge());

        assertTrue(provider.getOperandsE().isEmpty());
        assertTrue(provider.getOperandsF().isEmpty());
    }

    @Test
    public void testCaseADv2() throws FileNotFoundException {
        final NoLadderC4ECGraph graph = NoLadderC4ECGraph.loadFromFile(loadResourceAsStream("gadgetA/gadgetDv2.txt"));
        final NoLadderC4ECOperandProvider provider = new NoLadderC4ECOperandProvider(graph, new CachingCoder(graph));

        final List<Operand> operandsE = provider.getOperandsE();
        assertEquals(1, operandsE.size());
        final Operand operandE = operandsE.get(0);
        assertEquals(graph.getEdge(19, 20), operandE.getEdge());

        final List<Operand> operandsF = provider.getOperandsF();
        assertEquals(1, operandsF.size());
        final Operand operandF = operandsF.get(0);
        assertEquals(graph.getEdge(3, 11), operandF.getEdge());
        assertEquals(graph.getFace(3, 4, 20), operandF.getFace());
    }

    @Test
    public void testCaseADv3() throws FileNotFoundException {
        final NoLadderC4ECGraph graph = NoLadderC4ECGraph.loadFromFile(loadResourceAsStream("gadgetA/gadgetDv3.txt"));
        final NoLadderC4ECOperandProvider provider = new NoLadderC4ECOperandProvider(graph, new CachingCoder(graph));

        assertTrue(provider.getOperandsE().isEmpty());

        final List<Operand> operandsF = provider.getOperandsF();
        assertEquals(1, operandsF.size());
        final Operand operandF = operandsF.get(0);
        assertEquals(graph.getEdge(2, 12), operandF.getEdge());
        assertEquals(graph.getFace(2, 3, 23), operandF.getFace());
    }

    @Test
    public void testCaseAEF() throws FileNotFoundException {
        final NoLadderC4ECGraph graph = NoLadderC4ECGraph.loadFromFile(loadResourceAsStream("gadgetA/gadgetEF.txt"));
        final NoLadderC4ECOperandProvider provider = new NoLadderC4ECOperandProvider(graph, new CachingCoder(graph));

        assertEquals(4, provider.getOperandsA().size());
        assertEquals(4, provider.getOperandsB().size());
        assertEquals(1, provider.getOperandsC().size());
        assertEquals(1, provider.getOperandsD().size());

        final List<Operand> expectedOperandsE = List.of(
                new Operand(null, graph.getEdge(12, 16), null),
                new Operand(null, graph.getEdge(9, 10), null),
                new Operand(null, graph.getEdge(16, 17), null),
                new Operand(null, graph.getEdge(18, 19), null)
        );
        for (final Operand operandE : provider.getOperandsE()) {
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

        assertTrue(provider.getOperandsG().isEmpty());
        assertTrue(provider.getOperandsH().isEmpty());
    }

    @Test
    public void testCaseAEFv2() throws FileNotFoundException {
        final NoLadderC4ECGraph graph = NoLadderC4ECGraph.loadFromFile(loadResourceAsStream("gadgetA/gadgetEFv2.txt"));
        final NoLadderC4ECOperandProvider provider = new NoLadderC4ECOperandProvider(graph, new CachingCoder(graph));

        final List<Operand> operandsG = provider.getOperandsG();
        assertEquals(1, operandsG.size());
        assertEquals(graph.getEdge(4, 5), operandsG.get(0).getEdge());
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

    @Test
    public void testCaseBB() throws FileNotFoundException {
        final NoLadderC4ECGraph graph = NoLadderC4ECGraph.loadFromFile(loadResourceAsStream("gadgetB/gadgetB.txt"));
        final NoLadderC4ECOperandProvider provider = new NoLadderC4ECOperandProvider(graph, new CachingCoder(graph));

        final List<Operand> expectedOperandsA = List.of(
                new Operand(null, null, graph.getEdge(12, 9).getOriented(graph.getVertex(12))),
                new Operand(null, null, graph.getEdge(12, 11).getOriented(graph.getVertex(12))),
                new Operand(null, null, graph.getEdge(13, 14).getOriented(graph.getVertex(13))),
                new Operand(null, null, graph.getEdge(13, 16).getOriented(graph.getVertex(13))),
                new Operand(null, null, graph.getEdge(30, 28).getOriented(graph.getVertex(30))),
                new Operand(null, null, graph.getEdge(30, 29).getOriented(graph.getVertex(30))),
                new Operand(null, null, graph.getEdge(31, 32).getOriented(graph.getVertex(31))),
                new Operand(null, null, graph.getEdge(31, 33).getOriented(graph.getVertex(31)))
        );
        final List<Operand> operandsA = provider.getOperandsA();
        assertEquals(8, operandsA.size());
        for (final Operand operandA : operandsA) {
            assertTrue(expectedOperandsA.contains(operandA));
        }

        final List<Face> expectedOperandsB = List.of(
                graph.getFace(8, 9, 12),
                graph.getFace(4, 5, 7),
                graph.getFace(13, 14, 17),
                graph.getFace(18, 28, 30),
                graph.getFace(31, 32, 21),
                graph.getFace(23, 25, 26)
        );
        final List<Operand> operandsB = provider.getOperandsB();
        assertEquals(12, operandsB.size());
        operandsB.stream()
                .map(Operand::getFace)
                .distinct()
                .forEach(face -> assertTrue(expectedOperandsB.contains(face)));

        assertTrue(provider.getOperandsC().isEmpty());
        assertTrue(provider.getOperandsD().isEmpty());
        assertTrue(provider.getOperandsE().isEmpty());
        assertTrue(provider.getOperandsF().isEmpty());
        assertTrue(provider.getOperandsG().isEmpty());
        assertTrue(provider.getOperandsH().isEmpty());
    }

    @Test
    public void testCaseBCD() throws FileNotFoundException {
        for (final String input : new String[]{"gadgetB/gadgetC.txt", "gadgetB/gadgetD.txt"}) {
            final NoLadderC4ECGraph graph = NoLadderC4ECGraph.loadFromFile(loadResourceAsStream(input));
            final NoLadderC4ECOperandProvider provider = new NoLadderC4ECOperandProvider(graph, new CachingCoder(graph));

            assertEquals(4, provider.getOperandsA().size());
            assertEquals(10, provider.getOperandsB().size());

            final List<Operand> operandsC = provider.getOperandsC();
            assertEquals(1, operandsC.size());
            assertEquals(graph.getEdge(12, 13), operandsC.get(0).getEdge());

            assertTrue(provider.getOperandsD().isEmpty());
            assertTrue(provider.getOperandsE().isEmpty());
            assertTrue(provider.getOperandsF().isEmpty());
            assertTrue(provider.getOperandsG().isEmpty());
            assertTrue(provider.getOperandsH().isEmpty());
        }
    }

    @Test
    public void testCaseBEF() throws FileNotFoundException {
        final NoLadderC4ECGraph graph = NoLadderC4ECGraph.loadFromFile(loadResourceAsStream("gadgetB/gadgetEF.txt"));
        final NoLadderC4ECOperandProvider provider = new NoLadderC4ECOperandProvider(graph, new CachingCoder(graph));

        assertEquals(4, provider.getOperandsA().size());
        assertEquals(10, provider.getOperandsB().size());
        assertEquals(1, provider.getOperandsC().size());

        final List<Operand> expectedOperandsE = List.of(
                new Operand(null, graph.getEdge(9, 10), null),
                new Operand(null, graph.getEdge(14, 10), null),
                new Operand(null, graph.getEdge(11, 15), null),
                new Operand(null, graph.getEdge(16, 15), null)
        );
        final List<Operand> operandsE = provider.getOperandsE();
        assertEquals(4, operandsE.size());
        for (final Operand operandE : operandsE) {
            assertTrue(expectedOperandsE.contains(operandE));
        }

        final List<Operand> expectedOperandsF = List.of(
                new Operand(graph.getFace(9, 10, 14), graph.getEdge(12, 13), null),
                new Operand(graph.getFace(11, 12, 13), graph.getEdge(12, 13), null)
        );
        final List<Operand> operandsF = provider.getOperandsF();
        assertEquals(2, operandsF.size());
        for (final Operand operandF : operandsF) {
            assertTrue(expectedOperandsF.contains(operandF));
        }

        assertTrue(provider.getOperandsG().isEmpty());
        assertTrue(provider.getOperandsH().isEmpty());
    }

    @Test
    public void testCaseBG() throws FileNotFoundException {
        final NoLadderC4ECGraph graph = NoLadderC4ECGraph.loadFromFile(loadResourceAsStream("gadgetB/gadgetG.txt"));
        final NoLadderC4ECOperandProvider provider = new NoLadderC4ECOperandProvider(graph, new CachingCoder(graph));

        final List<Operand> operandsA = provider.getOperandsA();
        assertEquals(4, operandsA.size());
        final List<Operand> operandsB = provider.getOperandsB();
        assertEquals(10, operandsB.size());
        final List<Operand> operandsC = provider.getOperandsC();
        assertEquals(1, operandsC.size());
        final List<Operand> operandsD = provider.getOperandsD();
        assertTrue(operandsD.isEmpty());
        final List<Operand> operandsE = provider.getOperandsE();
        assertEquals(4, operandsE.size());
        final List<Operand> operandsF = provider.getOperandsF();
        assertEquals(2, operandsF.size());
        final List<Operand> operandsG = provider.getOperandsG();
        assertTrue(operandsG.isEmpty());
        final List<Operand> operandsH = provider.getOperandsH();
        assertTrue(operandsH.isEmpty());
    }

    @Test
    public void testCaseCC() throws FileNotFoundException {
        final NoLadderC4ECGraph graph = NoLadderC4ECGraph.loadFromFile(loadResourceAsStream("gadgetC/gadgetC.txt"));
        final NoLadderC4ECOperandProvider provider = new NoLadderC4ECOperandProvider(graph, new CachingCoder(graph));

        assertTrue(provider.getOperandsA().isEmpty());
        assertEquals(8, provider.getOperandsB().size());
        assertTrue(provider.getOperandsC().isEmpty());
        assertTrue(provider.getOperandsD().isEmpty());
        assertTrue(provider.getOperandsE().isEmpty());
        assertTrue(provider.getOperandsF().isEmpty());
    }

    @Test
    public void testCaseCD() throws FileNotFoundException {
        final NoLadderC4ECGraph graph = NoLadderC4ECGraph.loadFromFile(loadResourceAsStream("gadgetC/gadgetD.txt"));
        final NoLadderC4ECOperandProvider provider = new NoLadderC4ECOperandProvider(graph, new CachingCoder(graph));

        assertTrue(provider.getOperandsA().isEmpty());
        assertEquals(8, provider.getOperandsB().size());
        assertTrue(provider.getOperandsC().isEmpty());

        final List<Operand> expectedOperandsD = List.of(
                new Operand(null, graph.getEdge(13, 14), null),
                new Operand(null, graph.getEdge(39, 16), null),
                new Operand(null, graph.getEdge(38, 11), null),
                new Operand(null, graph.getEdge(12, 9), null)
        );
        final List<Operand> operandsD = provider.getOperandsD();
        assertEquals(4, operandsD.size());
        for (final Operand operandD : operandsD) {
            assertTrue(expectedOperandsD.contains(operandD));
        }
        assertTrue(provider.getOperandsE().isEmpty());
        assertTrue(provider.getOperandsF().isEmpty());
    }

    @Test
    public void testCaseCDv2() throws FileNotFoundException {
        final NoLadderC4ECGraph graph = NoLadderC4ECGraph.loadFromFile(loadResourceAsStream("gadgetC/gadgetDv2.txt"));
        final NoLadderC4ECOperandProvider provider = new NoLadderC4ECOperandProvider(graph, new CachingCoder(graph));

        assertTrue(provider.getOperandsA().isEmpty());
        assertEquals(6, provider.getOperandsB().size());
        assertTrue(provider.getOperandsC().isEmpty());
        assertEquals(4, provider.getOperandsD().size());
        final List<Operand> operandsF = provider.getOperandsF();
        assertEquals(1, operandsF.size());
        assertTrue(operandsF.contains(new Operand(graph.getFace(3, 4, 18), graph.getEdge(4, 18), null)));
        assertTrue(provider.getOperandsE().isEmpty());
    }

    @Test
    public void testCaseCEF() throws FileNotFoundException {
        final NoLadderC4ECGraph graph = NoLadderC4ECGraph.loadFromFile(loadResourceAsStream("gadgetC/gadgetEF.txt"));
        final NoLadderC4ECOperandProvider provider = new NoLadderC4ECOperandProvider(graph, new CachingCoder(graph));

        assertTrue(provider.getOperandsA().isEmpty());
        assertEquals(8, provider.getOperandsB().size());
        assertTrue(provider.getOperandsC().isEmpty());
        assertEquals(4, provider.getOperandsD().size());
        assertTrue(provider.getOperandsE().isEmpty());

        final List<Operand> operandsF = provider.getOperandsF();
        final List<Operand> expectedOperandsF = List.of(
                new Operand(graph.getFace(10, 42, 43), graph.getEdge(42, 43), null),
                new Operand(graph.getFace(9, 10, 14), graph.getEdge(13, 14), null),
                new Operand(graph.getFace(9, 10, 14), graph.getEdge(9, 12), null),
                new Operand(graph.getFace(8, 9, 12), graph.getEdge(9, 12), null),
                new Operand(graph.getFace(8, 9, 12), graph.getEdge(11, 40), null),
                new Operand(graph.getFace(11, 40, 41), graph.getEdge(11, 40), null),
                new Operand(graph.getFace(11, 40, 41), graph.getEdge(41, 16), null),
                new Operand(graph.getFace(13, 14, 17), graph.getEdge(41, 16), null),
                new Operand(graph.getFace(13, 14, 17), graph.getEdge(13, 14), null)
        );
        assertEquals(9, expectedOperandsF.size());
        for (final Operand operandF : operandsF) {
            assertTrue(expectedOperandsF.contains(operandF));
        }
        assertTrue(provider.getOperandsG().isEmpty());
        assertTrue(provider.getOperandsH().isEmpty());

    }
}
