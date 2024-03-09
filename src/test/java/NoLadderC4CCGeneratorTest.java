import graph.canoniccoders.CachingCoder;
import graph.canoniccoders.LexCode;
import graph.components.NoLadderC4ECGraph;
import graph.generators.noladderc4ec.NoLadderC4ECGenerator;
import graph.processors.Processor;
import graph.utils.EasyList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import static org.junit.Assert.*;


public class NoLadderC4CCGeneratorTest {

    private static final List<LexCode> baseLexCodes = EasyList.of(
            LexCode.builder() //B -> B1
                    .shortAddAll(EasyList.of(4,5,5,4,6,6,6,6,5,4,5,4))
                    .extendedAddAll(EasyList.of(
                            4,5,5,4,6,6,
                            0,0,6,0,0,
                            0,0,0,6,0,
                            0,0,0,0,
                            0,0,0,5,4,0,
                            0,0,0,5,0,0,
                            0,0,0,4,0,0)).build(),
            LexCode.builder() //B -> B2
                    .shortAddAll(EasyList.of(4,5,5,4,6,6,6,6,4,5,4,5))
                    .extendedAddAll(EasyList.of(
                            4,5,5,4,6,6,
                            0,0,6,0,0,
                            0,0,0,6,0,
                            0,0,0,0,
                            0,0,0,4,5,0,
                            0,0,0,4,0,0,
                            0,0,0,5,0,0)).build(),
            LexCode.builder() //B -> D
                    .shortAddAll(EasyList.of(4,5,5,5,6,4,5,5,4,5))
                    .extendedAddAll(EasyList.of(
                            4,5,5,5,6,
                            0,0,4,5,0,
                            0,0,0,5,0,
                            0,0,0,4,0,
                            0,0,0,5,0,0)).build(),
            LexCode.builder() //B -> F
                    .shortAddAll(EasyList.of(4,5,5,6,6,4,6,4,5,4,5))
                    .extendedAddAll(EasyList.of(
                            4,5,5,6,6,
                            0,0,4,6,0,
                            0,0,0,4,0,
                            0,0,0,5,4,0,
                            0,0,0,5,0,0)).build(),
            LexCode.builder() //B -> C -> B
                    .shortAddAll(EasyList.of(4,5,5,4,6,6,6,6,5,5,5,5,4))
                    .extendedAddAll(EasyList.of(
                            4,5,5,4,6,6,
                            0,0,6,0,0,
                            0,0,0,6,0,
                            0,0,0,0,
                            0,0,0,5,5,0,
                            0,0,0,5,0,0,
                            0,0,0,5,0,0,
                            0,0,0,0,0,0,
                            0,0,0,4,0)).build(),
            LexCode.builder() //B -> C -> D
                    .shortAddAll(EasyList.of(4,5,5,5,6,5,5,5,5,4,5))
                    .extendedAddAll(EasyList.of(
                            4,5,5,5,6,
                            0,0,5,5,0,
                            0,0,0,5,0,
                            0,0,0,5,0,
                            0,0,0,4,0,0,
                            0,0,0,5,0)).build()
    );

    @Test
    public void testBaseGraphCases() {
        final Set<LexCode> resultCodes = new HashSet<>();

        final NoLadderC4ECGraph graph = new NoLadderC4ECGraph();
        graph.init();

        final NoLadderC4ECGenerator generator = new NoLadderC4ECGenerator(graph, new Processor(graph, null, null, null) {
            @Override
            public boolean hasProperty() {
                final CachingCoder coder = new CachingCoder(graph);
                assertTrue(baseLexCodes.contains(coder.getGraphCode()));
                resultCodes.add(coder.getGraphCode());
                return true;
            }
        }, 0);
        generator.generate();
        assertEquals(6, resultCodes.stream().distinct().count());
    }

    @Test
    public void testGeneration() {
        final NoLadderC4ECGraph noLadderC4ECGraph = new NoLadderC4ECGraph();
        noLadderC4ECGraph.init();
        final int depth = 24;
        final NoLadderC4ECGenerator generator = new NoLadderC4ECGenerator(noLadderC4ECGraph, new Processor(noLadderC4ECGraph, null, null, null) {
            @Override
            public boolean hasProperty() {
                if (graph.getVertices().size() == depth) {
                    System.out.println(noLadderC4ECGraph.toShortString());
                }
                return true;
            }
        }, depth);
        generator.generate();
    }


    @Test
    public void testSingularity() {
        final NoLadderC4ECGraph noLadderC4ECGraph = new NoLadderC4ECGraph();
        noLadderC4ECGraph.init();

        final Set<LexCode> lexCodes = new HashSet<>();
        final CachingCoder coder = new CachingCoder(noLadderC4ECGraph);
        final NoLadderC4ECGenerator generator = new NoLadderC4ECGenerator(noLadderC4ECGraph, new Processor(noLadderC4ECGraph, null, null, null) {
            @Override
            public boolean hasProperty() {
                final LexCode currentCode = coder.getGraphCode();
                if (lexCodes.contains(currentCode)) {
                    fail();
                } else {
                    lexCodes.add(currentCode);
                }
                return true;
            }
        }, 28);
    }
}
