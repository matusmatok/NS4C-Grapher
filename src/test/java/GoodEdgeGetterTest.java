import graph.components.Edge;
import graph.components.Graph;
import graph.processors.GoodEdgeGetter;
import java.util.List;
import org.junit.Test;

public class GoodEdgeGetterTest {

    private static final Graph graph = new TutteFragmentMock();
    private static final GoodEdgeGetter goodEdgeGetter = new GoodEdgeGetter(graph);

    @Test
    public void testGetGoodEdges(){
        List<Edge> result = goodEdgeGetter.getGoodEdges();
        for (Edge goodEdge : result) {
            System.out.println(goodEdge.getId());
        }
    }

    @Test
    public void testIsEdgeGood() {
        System.out.println(goodEdgeGetter.isEdgeGood(1));
    }
}
