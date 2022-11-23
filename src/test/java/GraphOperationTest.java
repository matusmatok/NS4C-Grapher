import static org.junit.Assert.assertEquals;

import graph.components.Edge;
import graph.components.Graph;
import org.junit.Test;

public class GraphOperationTest {
    private static final Graph graph = new Graph();

    @Test
    public void testOp4_5() {
        graph.init();
        graph.op3(graph.getVertices().get(0));
        graph.toNormalForm();
        final String g1 = graph.toString();
        graph.print();
        System.out.println("----END----");
        final Edge middleEdge = graph.op4(graph.getFaces().get(0), 0);
        graph.toNormalForm();
        final String g2 = graph.toString();
        graph.print();
        System.out.println("----END----");
        final Edge middleEdge2 = graph.op5(graph.getFaces().get(1), 0);
        graph.toNormalForm();
        graph.print();
        System.out.println("----END----");
        graph.undoOp5(middleEdge2);
        graph.toNormalForm();
        final String g3 = graph.toString();
        graph.print();
        System.out.println("----END----");
        graph.undoOp4(middleEdge);
        graph.toNormalForm();
        final String g4 = graph.toString();
        graph.print();

        assertEquals(g1, g4);
        assertEquals(g2, g3);

    }


}
