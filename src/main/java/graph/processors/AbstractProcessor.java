package graph.processors;

import graph.builders.AbstractTwoFactorBuilder;
import graph.components.Edge;
import graph.components.AbstractGraph;
import graph.components.Vertex;
import graph.preprocessors.AmbiguousVertexGetter;
import graph.preprocessors.HamEdgeGetter;
import graph.preprocessors.ReducibleConfigurationFinder;
import graph.preprocessors.VertexPolarityDistributor;
import java.util.List;

public abstract class AbstractProcessor {

    protected final AbstractGraph graph;

    protected final HamEdgeGetter potentialEdgeGetter;

    private final AmbiguousVertexGetter ambiguousVertexGetter;
    private final AbstractTwoFactorBuilder builder;
    private final ReducibleConfigurationFinder finder;

    public AbstractProcessor(AbstractGraph graph, HamEdgeGetter potentialEdgeGetter,
                             AmbiguousVertexGetter ambiguousVertexGetter, AbstractTwoFactorBuilder builder) {
        this.graph = graph;
        this.potentialEdgeGetter = potentialEdgeGetter;
        this.ambiguousVertexGetter = ambiguousVertexGetter;
        this.builder = builder;
        finder = new ReducibleConfigurationFinder(graph);
    }
    public boolean hasProperty() {
        if (finder.containsPolarIndependentReducibleConfig()) return false;
        final List<Edge> potentiallyGoodEdges = potentialEdgeGetter.getResult();
        for (Edge potentiallyGoodEdge : potentiallyGoodEdges) {
            final List<Vertex> ambiguousVertices = ambiguousVertexGetter.getResult(potentiallyGoodEdge);
            final VertexPolarityDistributor distributor = new VertexPolarityDistributor(graph, ambiguousVertices);
            for (AbstractGraph polarisedGraph : distributor) {
                /* containsPolarDependentReducibleConfig is faulty, needs fixing
                finder.buildCache(new HashSet<>(ambiguousVertices));
                if (!finder.containsPolarDependentReducibleConfig()) {
                    if (!builder.canBuild(potentiallyGoodEdge)) return true;
                }*/
                if (!builder.canBuild(potentiallyGoodEdge)) {
                    System.out.println("GOOD EDGE " + potentiallyGoodEdge.getId() + ":");
                    return true;
                }
            }
        }
        return false;
    }
}
