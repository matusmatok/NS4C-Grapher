import static org.junit.Assert.assertEquals;

import graph.components.NoLadderC4ECGraph;
import java.util.Iterator;

import graph.generators.noladderc4ec.NoLadderC4ECGenerator;
import graph.processors.AbstractProcessor;
import org.junit.Test;
import plantri.NoLadderFilter;
import plantri.PlantriOutputReader;

public class PlantriSetupTest implements ResourceDependent {

    @Test
    public void run() {
        PlantriOutputReader por = new PlantriOutputReader(loadResourceAsStream("/plantriOutputs/28vertices.txt"));
        Iterator<NoLadderC4ECGraph> iterator = por.iterator();

        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        assertEquals(30926, count);
    }
}
