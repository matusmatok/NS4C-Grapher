package graph.processors;

import graph.builders.ConsistentTwoFactorBuilder;
import graph.components.Edge;
import graph.components.Graph;
import graph.components.Vertex;
import graph.preprocessors.AmbiguousVertexGetter;
import graph.preprocessors.GoodEdgeGetter;
import graph.preprocessors.ReducibleConfigurationFinder;
import graph.preprocessors.VertexPolarityDistributor;

import java.util.HashSet;
import java.util.List;

public class Processor extends AbstractProcessor {

    private final Graph graph;
    private final GoodEdgeGetter goodEdgeGetter;
    private final AmbiguousVertexGetter ambiguousVertexGetter;
    private final ConsistentTwoFactorBuilder builder;
    private final ReducibleConfigurationFinder finder;

    public Processor(Graph graph) {
        this.graph = graph;
        goodEdgeGetter = new GoodEdgeGetter(graph);
        ambiguousVertexGetter = new AmbiguousVertexGetter(graph);
        builder = new ConsistentTwoFactorBuilder(graph);
        finder = new ReducibleConfigurationFinder(graph);
    }

    @Override
    public boolean hasProperty() {
        if (finder.containsPolarIndependentReducibleConfig()) return false;
        final List<Edge> potentiallyGoodEdges = goodEdgeGetter.getResult();
        for (Edge potentiallyGoodEdge : potentiallyGoodEdges) {
            final List<Vertex> ambiguousVertices = ambiguousVertexGetter.getResult(potentiallyGoodEdge);
            final VertexPolarityDistributor distributor = new VertexPolarityDistributor(graph, ambiguousVertices);
            for (Graph polarisedGraph : distributor) {
                finder.buildCache(new HashSet<>(ambiguousVertices));
                if (!finder.containsPolarDependentReducibleConfig()) {
                    if (!builder.canBuild(potentiallyGoodEdge)) return true;
                }
            }
        }
        return false;
    }
}
