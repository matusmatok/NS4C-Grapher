package graph.preprocessors.ambiguous;

import graph.components.Edge;
import graph.components.AbstractGraph;
import graph.components.Vertex;
import graph.preprocessors.ambiguous.AmbiguousVertexGetter;

import java.util.ArrayList;
import java.util.List;

public class UntouchableAmbiguousVertexGetter extends AmbiguousVertexGetter {

    public UntouchableAmbiguousVertexGetter(AbstractGraph graph) {
        super(graph);
    }
    
    @Override
    public List<Vertex> getResult(Edge potentialEdge) {
        final List<Vertex> ambiguousVertices = new ArrayList<>();
        for (Vertex v : graph.getVertices()) {
            for (Edge e : v.getAdjacentEdges()) {
                excludeOmittedEdge(e, v);
            }
            if (v.getAdjacentEdges().contains(potentialEdge)) {
                ambiguousVertices.add(v);
            }
            else if (includeEdge(potentialEdge)) {
                unionFindSet.union(potentialEdge.getVTo(), potentialEdge.getVFrom());
                if (!cycleConstructable(v.getAdjacentEdges().get(0).getOppositeVertex(v))) ambiguousVertices.add(v);
                unionFindSet.disunion();
                undoIncludeEdge(potentialEdge);
            }
            else {
                ambiguousVertices.add(v);
            }
            for (Edge e : v.getAdjacentEdges()) {
                undoExcludeOmittedEdge(e, v);
            }
        }
        return ambiguousVertices;
    }
}
