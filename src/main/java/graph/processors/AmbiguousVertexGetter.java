package graph.processors;

import graph.components.Edge;
import graph.components.Graph;
import graph.components.Vertex;

import java.util.ArrayList;
import java.util.List;

import static graph.components.Edge.hamCycleEdgeState.EXCLUDED;
import static graph.components.Vertex.HamCycleVertexState.*;

/*AmbiguousVertexGetter returns a list of ambiguous vertices for a given "good" edge.
 *The definition of an ambiguous vertex is as follows:
 * If for a fixed good edge 'e' there exists no cycle covering all vertices but 'v',
 * not containing the edge 'e', then 'v' is an ambiguous vertex.
 * */
public class AmbiguousVertexGetter extends AbstractPreProcessor<Vertex> {

    private final Edge goodEdge;

    public AmbiguousVertexGetter(Graph g, Edge goodEdge) {
        super(g, new SimpleUnionFindSet());
        this.goodEdge = goodEdge;
    }

    @Override
    public List<Vertex> getResult() {
        List<Vertex> ambiguousVertices = new ArrayList<>();
        for (Vertex v : graph.getVertices()) {
            for (Edge e : v.getAdjacentEdges()) excludeOmittedEdge(e, v);
            if (v.getAdjacentEdges().contains(goodEdge)) {
                if (!cycleConstructible()) ambiguousVertices.add(v);
            }
            else if (excludeEdge(goodEdge)) {
                if (!cycleConstructible()) ambiguousVertices.add(v);
                undoExcludeEdge(goodEdge);
            }
            else {
                ambiguousVertices.add(v);
            }
            for (Edge e : v.getAdjacentEdges()) undoExcludeOmittedEdge(e, v);
        }
        return ambiguousVertices;
    }

    @Override
    public boolean canCloseCycle(Vertex color) {
        return color.getSubtreeSize() == graph.getVertices().size() - 1;
    }

    private void excludeOmittedEdge(Edge edge, Vertex omittedVertex) {
        final Vertex otherVertex = edge.getOppositeVertex(omittedVertex);
        otherVertex.setHamCycleState(DETERMINISTIC_INCLUDE_REST);
        undiscoveredVertices.remove(otherVertex);
        deterministicVertices.add(otherVertex);
        edge.setHamCycleState(EXCLUDED);
    }
    private void undoExcludeOmittedEdge(Edge edge, Vertex omittedVertex) {
        final Vertex otherVertex = edge.getOppositeVertex(omittedVertex);
        otherVertex.setHamCycleState(UNDISCOVERED);
        deterministicVertices.remove(otherVertex);
        undiscoveredVertices.add(otherVertex);
        edge.setHamCycleState(Edge.hamCycleEdgeState.UNDISCOVERED);
    }
}
