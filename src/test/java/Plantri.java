import static org.junit.Assert.assertEquals;

import graph.components.NoLadderC4ECGraph;
import java.util.Iterator;
import org.junit.Test;
import plantri.PlantriOutputReader;

public class Plantri implements ResourceDependent {

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
