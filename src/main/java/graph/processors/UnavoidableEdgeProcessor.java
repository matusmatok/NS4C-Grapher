package graph.processors;

import graph.builders.ConsistentTwoFactorBuilder;
import graph.components.Edge;
import graph.components.Graph;
import graph.components.Vertex;
import graph.preprocessors.AmbiguousVertexGetter;
import graph.preprocessors.HamUnavoidableEdgeGetter;
import graph.preprocessors.ReducibleConfigurationFinder;
import graph.preprocessors.VertexPolarityDistributor;

import java.util.List;

public class UnavoidableEdgeProcessor extends AbstractProcessor {

    protected final Graph graph;
    private final HamUnavoidableEdgeGetter hamUnavoidableEdgeGetter;
    private final AmbiguousVertexGetter ambiguousVertexGetter;
    private final ConsistentTwoFactorBuilder builder;
    private final ReducibleConfigurationFinder finder;

    public UnavoidableEdgeProcessor(Graph graph) {
        this.graph = graph;
        hamUnavoidableEdgeGetter = new HamUnavoidableEdgeGetter(graph);
        ambiguousVertexGetter = new AmbiguousVertexGetter(graph);
        builder = new ConsistentTwoFactorBuilder(graph);
        finder = new ReducibleConfigurationFinder(graph);
    }

    @Override
    public boolean hasProperty() {
        if (finder.containsPolarIndependentReducibleConfig()) return false;
        final List<Edge> potentiallyGoodEdges = hamUnavoidableEdgeGetter.getResult();
        for (Edge potentiallyGoodEdge : potentiallyGoodEdges) {
            final List<Vertex> ambiguousVertices = ambiguousVertexGetter.getResult(potentiallyGoodEdge);
            final VertexPolarityDistributor distributor = new VertexPolarityDistributor(graph, ambiguousVertices);
            for (Graph polarisedGraph : distributor) {
                /* containsPolarDependentReducibleConfig is faulty, needs fixing
                finder.buildCache(new HashSet<>(ambiguousVertices));
                if (!finder.containsPolarDependentReducibleConfig()) {
                    if (!builder.canBuild(potentiallyGoodEdge)) return true;
                }*/
                if (!builder.canBuild(potentiallyGoodEdge)) {
                    System.out.println("UNAVOIDABLE EDGE " + potentiallyGoodEdge.getId() + ":");
                    return true;
                }
            }
        }
        return false;
    }
}
