package graph.preprocessors.distributors;

import graph.components.AbstractGraph;
import graph.components.Vertex;

import java.util.List;

public abstract class AbstractDistributor implements Iterable<AbstractGraph> {

    public abstract static class DistributorFactory {
        public abstract AbstractDistributor produceDistributor(final AbstractGraph g, final List<Vertex> ambiguousVertices);
    }
}
