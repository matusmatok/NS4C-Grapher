package graph.preprocessors.ambiguous;

import graph.components.AbstractGraph;
import graph.components.Edge;
import graph.components.Vertex;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Class0AmbiguousVertexGetter extends AmbiguousVertexGetter{

    /**
     * g is expected to be non-hamiltonian*/
    public Class0AmbiguousVertexGetter(AbstractGraph g) {
        super(g);
    }


    /**
     * pick arbitrary edge, does not matter*/
    @Override
    public List<Vertex> getResult(Edge potentialEdge) {
        final List<Vertex> potentiallyAmbiguousVertices = new ArrayList<>(graph.getVertices());

        for (final Edge e : graph.getEdges()) {
            final Set<Vertex> localNonAmbiguousVertices = new HashSet<>();
            for (final Vertex v : potentiallyAmbiguousVertices) {
                if (!isAmbiguousExcludeEdge(v, e) || !isAmbiguousIncludeEdge(v, e)) {
                    localNonAmbiguousVertices.add(v);
                }
            }
            potentiallyAmbiguousVertices.removeAll(localNonAmbiguousVertices);
        }

        return potentiallyAmbiguousVertices;
    }
}
