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
            if (isAmbiguousIncludeEdge(v, potentialEdge)) ambiguousVertices.add(v);
        }
        return ambiguousVertices;
    }
}
