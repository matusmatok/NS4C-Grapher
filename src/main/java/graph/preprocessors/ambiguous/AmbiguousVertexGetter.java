package graph.preprocessors.ambiguous;

import graph.components.Edge;
import graph.components.AbstractGraph;
import graph.components.Vertex;
import graph.preprocessors.AbstractPreProcessor;
import graph.utils.SimpleUnionFindSet;

import java.util.List;

import static graph.components.Edge.hamCycleEdgeState.EXCLUDED;
import static graph.components.Vertex.CycleVertexState.DETERMINISTIC_INCLUDE_REST;
import static graph.components.Vertex.CycleVertexState.UNDISCOVERED;

public abstract class AmbiguousVertexGetter extends AbstractPreProcessor<Vertex> {

    public AmbiguousVertexGetter(AbstractGraph g) {
        super(g, new SimpleUnionFindSet());
    }

    public abstract List<Vertex> getResult(Edge potentialEdge);

    @Override
    public boolean canCloseCycle(Vertex color) {
        return color.getSubtreeSize() == graph.getVertices().size() - 1;
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

    protected void excludeOmittedVertex(final Vertex vertex) {
        for (Edge e : vertex.getAdjacentEdges()) excludeOmittedEdge(e, vertex);
    }

    protected void undoExcludeOmittedVertex(final Vertex vertex) {
        for (Edge e : vertex.getAdjacentEdges()) undoExcludeOmittedEdge(e, vertex);
    }

    protected boolean isAmbiguousExcludeEdge(final Vertex vertex, final Edge edge) {
        excludeOmittedVertex(vertex);

        boolean returnValue = false;

        if (vertex.getAdjacentEdges().contains(edge)) {
            if (!cycleConstructable(vertex.getAdjacentEdges().get(0).getOppositeVertex(vertex))) returnValue = true;
        }
        else if (excludeEdge(edge)) {
            if (!cycleConstructable(vertex.getAdjacentEdges().get(0).getOppositeVertex(vertex))) returnValue = true;
            undoExcludeEdge(edge);
        }
        else {
            returnValue = true;
        }

        undoExcludeOmittedVertex(vertex);

        return returnValue;
    }

    protected boolean isAmbiguousIncludeEdge(final Vertex vertex, final Edge edge) {
        excludeOmittedVertex(vertex);

        boolean returnValue = false;

        if (vertex.getAdjacentEdges().contains(edge)) {
            returnValue = true;
        }
        else if (includeEdge(edge)) {
            unionFindSet.union(edge.getVTo(), edge.getVFrom());
            if (!cycleConstructable(vertex.getAdjacentEdges().get(0).getOppositeVertex(vertex))) {
                returnValue = true;
            }
            unionFindSet.disunion();
            undoIncludeEdge(edge);
        }
        else {
            returnValue = true;
        }

        undoExcludeOmittedVertex(vertex);

        return returnValue;
    }
}
