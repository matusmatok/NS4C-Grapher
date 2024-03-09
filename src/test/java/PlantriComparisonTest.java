import static org.junit.Assert.assertTrue;

import graph.canoniccoders.CanonicalCoder;
import graph.canoniccoders.LexCode;
import graph.components.NoLadderC4ECGraph;
import graph.generators.noladderc4ec.NoLadderC4ECGenerator;
import graph.processors.Processor;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import plantri.NoLadderFilter;

@RunWith(Parameterized.class)
public class PlantriComparisonTest implements ResourceDependent {

    @Parameterized.Parameters
    public static Collection<Integer> data() {
        return Arrays.asList(20,22,24,26,28);
    }

    private final HashSet<LexCode> generatedGraphs;
    private final NoLadderFilter filter;

    public PlantriComparisonTest(final int depth) {
        generatedGraphs = new HashSet<>();
        final NoLadderC4ECGraph graph = new NoLadderC4ECGraph();
        graph.init();
        final NoLadderC4ECGenerator generator = new NoLadderC4ECGenerator(graph, new Processor(graph, null, null, null) {
            @Override
            public boolean hasProperty() {
                generatedGraphs.add(CanonicalCoder.getCodeForGraph(graph));
                return true;
            }
        }, depth);
        generator.generate();

        filter = new NoLadderFilter(loadResourceAsStream("/plantriOutputs/" + depth + "vertices.txt"));
    }

    @Test
    public void testEquality() {
        boolean all = true;
        for (final NoLadderC4ECGraph graph : filter) {
            final LexCode plantriCode = CanonicalCoder.getCodeForGraph(graph);
            if (!generatedGraphs.contains(plantriCode)) {
                all = false;
                System.out.print(plantriCode);
                System.out.print(graph.toShortString());
            }
        }
        assertTrue(all);
    }
}
