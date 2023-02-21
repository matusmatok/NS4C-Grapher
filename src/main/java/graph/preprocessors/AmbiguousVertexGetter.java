package graph.preprocessors;

import static graph.components.Edge.hamCycleEdgeState.EXCLUDED;
import static graph.components.Vertex.CycleVertexState.DETERMINISTIC_INCLUDE_REST;
import static graph.components.Vertex.CycleVertexState.UNDISCOVERED;

import graph.components.Edge;
import graph.components.Graph;
import graph.components.Vertex;
import graph.utils.SimpleUnionFindSet;
import java.util.List;

public abstract class AmbiguousVertexGetter extends AbstractPreProcessor<Vertex> {

    public AmbiguousVertexGetter(Graph g) {
        super(g, new SimpleUnionFindSet());
    }

    public abstract List<Vertex> getResult(Edge potentialEdge);

    @Override
    protected boolean canCloseCycle(Vertex color) {
        return false;
    }

    protected void excludeOmittedEdge(Edge edge, Vertex omittedVertex) {
        final Vertex otherVertex = edge.getOppositeVertex(omittedVertex);
        otherVertex.setCycleState(DETERMINISTIC_INCLUDE_REST);
        undiscoveredVertices.remove(otherVertex);
        deterministicVertices.add(otherVertex);
        edge.setHamCycleState(EXCLUDED);
    }
    protected void undoExcludeOmittedEdge(Edge edge, Vertex omittedVertex) {
        final Vertex otherVertex = edge.getOppositeVertex(omittedVertex);
        otherVertex.setCycleState(UNDISCOVERED);
        deterministicVertices.remove(otherVertex);
        undiscoveredVertices.add(otherVertex);
        edge.setHamCycleState(Edge.hamCycleEdgeState.UNDISCOVERED);
    }
}
