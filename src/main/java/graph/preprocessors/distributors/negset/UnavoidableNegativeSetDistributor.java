package graph.preprocessors.distributors.negset;

import graph.components.AbstractGraph;
import graph.components.Edge;
import graph.components.Vertex;
import graph.preprocessors.distributors.negset.provider.AbstractNegSetsProvider;
import graph.preprocessors.util.VertexSetOmittingHamUtil;

import java.util.List;

public class UnavoidableNegativeSetDistributor extends NegativeSetsDistributor{

    public UnavoidableNegativeSetDistributor(AbstractGraph graph, Edge potentialEdge, List<Vertex> cubes) {
        super(graph, potentialEdge, cubes);
    }

    public UnavoidableNegativeSetDistributor(AbstractGraph graph, Edge potentialEdge, AbstractNegSetsProvider potentialSetProvider, List<Vertex> cubes) {
        super(graph, potentialEdge, potentialSetProvider, cubes);
    }

    @Override
    protected boolean action(final Edge potentialEdge, final List<Vertex> potentialSet) {
        return VertexSetOmittingHamUtil.isUnavoidConstructable(this.getGraph(), potentialSet, potentialEdge);
    }
}
