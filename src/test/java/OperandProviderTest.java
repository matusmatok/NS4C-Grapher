import graph.components.Graph;
import graph.generators.OperandProvider;
import mock.TutteFragmentMock;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class OperandProviderTest {
    private static final OperandProvider provider = new OperandProvider();

    @Test
    public void testGetOp3_4() {
        final Graph tutteFragmentMock = new TutteFragmentMock();
        final List<OperandProvider.Operand> operands = provider.getOp3Operands(tutteFragmentMock);
        assertEquals(16, operands.size());
        final Graph simpleGraph = new Graph();

        simpleGraph.init();
        simpleGraph.op3(0); //trivial state
        final List<OperandProvider.Operand> operands1 = provider.getOp3Operands(simpleGraph);
        final List<OperandProvider.Operand> operands10 = provider.getOp4Operands(simpleGraph);
        assertEquals(6, operands1.size());
        assertEquals(3, operands10.size());

        simpleGraph.op3(1); // now graph contains two triangles having a mutual outgoing edge
        final List<OperandProvider.Operand> operands2 = provider.getOp3Operands(simpleGraph);
        final List<OperandProvider.Operand> operands9 = provider.getOp4Operands(simpleGraph);
        assertEquals(operands2.size(), 2);
        assertEquals(operands9.size(), 1);

        simpleGraph.op3(7); // now graph contains two triangles with no mutual outgoing edge
        final List<OperandProvider.Operand> operands3 = provider.getOp3Operands(simpleGraph);
        final List<OperandProvider.Operand> operands8 = provider.getOp4Operands(simpleGraph);
        assertEquals(operands3.size(), 0);
        assertEquals(operands8.size(), 0);

        simpleGraph.op4(3, 13); // now graph contains one triangle
        final List<OperandProvider.Operand> operands4 = provider.getOp3Operands(simpleGraph);
        final List<OperandProvider.Operand> operands7 = provider.getOp4Operands(simpleGraph);
        assertEquals(operands4.size(), 6);
        assertEquals(operands7.size(), 3);

        simpleGraph.op4(0, 6); // now graph contains no triangle
        final List<OperandProvider.Operand> operands5 = provider.getOp3Operands(simpleGraph);
        final List<OperandProvider.Operand> operands6 = provider.getOp4Operands(simpleGraph);
        assertEquals(operands5.size(), simpleGraph.getVertices().size());
        assertEquals(operands6.size(), simpleGraph.getEdges().size());
    }
}
