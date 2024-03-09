package graph.processors;

import graph.builders.AbstractTwoFactorBuilder;
import graph.components.Edge;
import graph.components.AbstractGraph;
import graph.components.Vertex;
import graph.preprocessors.ambiguous.AmbiguousVertexGetter;
import graph.preprocessors.HamEdgeGetter;
import graph.preprocessors.reducible.AbstractFinder;
import graph.preprocessors.reducible.ReducibleConfigurationFinder;
import graph.preprocessors.distributors.AbstractDistributor;
import graph.preprocessors.distributors.VertexPolarityDistributor;
import java.util.List;

public class Processor {

    protected final AbstractGraph graph;

    protected final HamEdgeGetter potentialEdgeGetter;

    private final AmbiguousVertexGetter ambiguousVertexGetter;
    private final AbstractTwoFactorBuilder builder;
    private final AbstractFinder finder;
    private final AbstractDistributor.DistributorFactory distributorFactory;

    public Processor(AbstractGraph graph, HamEdgeGetter potentialEdgeGetter,
                     AmbiguousVertexGetter ambiguousVertexGetter, AbstractTwoFactorBuilder builder) {
        this.graph = graph;
        this.potentialEdgeGetter = potentialEdgeGetter;
        this.ambiguousVertexGetter = ambiguousVertexGetter;
        this.builder = builder;
        finder = new ReducibleConfigurationFinder(graph);
        this.distributorFactory = VertexPolarityDistributor.getFactory();
    }

    public Processor(AbstractGraph graph, HamEdgeGetter potentialEdgeGetter,
                     AmbiguousVertexGetter ambiguousVertexGetter,
                     AbstractDistributor.DistributorFactory distributorFactory,
                     AbstractFinder finder,
                     AbstractTwoFactorBuilder builder) {
        this.graph = graph;
        this.potentialEdgeGetter = potentialEdgeGetter;
        this.ambiguousVertexGetter = ambiguousVertexGetter;
        this.builder = builder;
        this.finder = finder;
        this.distributorFactory = distributorFactory;
    }

    public Processor(AbstractGraph graph, HamEdgeGetter potentialEdgeGetter,
                     AmbiguousVertexGetter ambiguousVertexGetter,
                     AbstractDistributor.DistributorFactory distributorFactory,
                     AbstractTwoFactorBuilder builder) {
        this.graph = graph;
        this.potentialEdgeGetter = potentialEdgeGetter;
        this.ambiguousVertexGetter = ambiguousVertexGetter;
        this.builder = builder;
        this.finder = new AbstractFinder() {
            @Override
            public boolean containsPolarIndependentReducibleConfig() {
                return false;
            }
        };
        this.distributorFactory = distributorFactory;
    }

    public boolean hasProperty() {
        if (finder.containsPolarIndependentReducibleConfig()) return false;
        final List<Edge> potentiallyGoodEdges = potentialEdgeGetter.getResult();
        for (Edge potentiallyGoodEdge : potentiallyGoodEdges) {
            final List<Vertex> ambiguousVertices = ambiguousVertexGetter.getResult(potentiallyGoodEdge);
            final AbstractDistributor distributor = distributorFactory.produceDistributor(graph, ambiguousVertices);
            for (AbstractGraph polarisedGraph : distributor) {
                if (!builder.canBuild(potentiallyGoodEdge)) {
                    System.out.println("GOOD EDGE " + potentiallyGoodEdge.getId() + ":");
                    return true;
                }
            }
        }
        return false;
    }
}
