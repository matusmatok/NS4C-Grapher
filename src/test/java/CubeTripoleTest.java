import graph.components.CubeTripole;
import graph.components.Graph;
import graph.components.Vertex;
import mock.ExtendedTenTuttovTotoMock;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class CubeTripoleTest {
    private static final Graph TEN_TUTTOV_TOTO = new ExtendedTenTuttovTotoMock();

    @Test
    public void testCubeTripoleFunctionality() {
        assertEquals(4, TEN_TUTTOV_TOTO.getCubes().size());
        final CubeTripole cube = TEN_TUTTOV_TOTO.getCubes().get(0);
        cube.distributePolarity();
        assertEquals(6, cube.getAllVertices().stream().filter(vertex -> vertex.getWeight() == 1).count());
        assertEquals(1, cube.getCentralVertex().getWeight());
        final List<Vertex> stepOneVertices = cube.getCentralVertex().getAdjacentEdges()
                .stream().map(edge -> edge.getOppositeVertex(cube.getCentralVertex()))
                .collect(Collectors.toList());
        assertEquals(2, stepOneVertices.stream().filter(vertex -> vertex.getWeight() == 1).count());
    }

}
