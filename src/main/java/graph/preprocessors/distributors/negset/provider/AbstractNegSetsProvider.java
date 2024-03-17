package graph.preprocessors.distributors.negset.provider;

import graph.components.AbstractGraph;
import graph.components.Vertex;
import java.util.List;

public abstract class AbstractNegSetsProvider {
    protected AbstractGraph graph;

    public AbstractNegSetsProvider(final AbstractGraph graph) {
        this.graph = graph;
    }

    public abstract List<List<Vertex>> providePotentialSets();
}
