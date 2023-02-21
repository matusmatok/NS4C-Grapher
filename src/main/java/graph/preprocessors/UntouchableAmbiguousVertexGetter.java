package graph.preprocessors;

import graph.components.Edge;
import graph.components.Graph;
import graph.components.Vertex;

import java.util.ArrayList;
import java.util.List;

public class UntouchableAmbiguousVertexGetter extends AmbiguousVertexGetter {

    public UntouchableAmbiguousVertexGetter(Graph graph) {
        super(graph);
    }
    
    @Override
    public List<Vertex> getResult(Edge potentialEdge) {
        final List<Vertex> ambiguousVertices = new ArrayList<>();
        for (Vertex v : graph.getVertices()) {
            if (v.getAdjacentEdges().contains(potentialEdge)) {
                ambiguousVertices.add(v);
            }
            else if (includeEdge(potentialEdge)) {
                if (!cycleConstructible()) ambiguousVertices.add(v);
                undoIncludeEdge(potentialEdge);
            }
            else {
                ambiguousVertices.add(v);
            }
        }
        return ambiguousVertices;
    }
}
