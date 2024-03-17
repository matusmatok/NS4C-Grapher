package graph.preprocessors.distributors.negset;

import graph.components.AbstractGraph;
import graph.components.Edge;
import graph.components.Vertex;
import graph.preprocessors.distributors.negset.provider.AbstractNegSetsProvider;
import graph.preprocessors.util.VertexSetOmittingHamUtil;

import java.util.List;

public class UntouchableNegativeSetDistributor extends NegativeSetsDistributor {

    public UntouchableNegativeSetDistributor(AbstractGraph graph, Edge potentialEdge, List<Vertex> cubes) {
        super(graph, potentialEdge, cubes);
    }

    public UntouchableNegativeSetDistributor(AbstractGraph graph, Edge potentialEdge, AbstractNegSetsProvider potentialSetProvider, List<Vertex> cubes) {
        super(graph, potentialEdge, potentialSetProvider, cubes);
    }

    @Override
    protected boolean action(Edge potentialEdge, List<Vertex> potentialSet) {
        return VertexSetOmittingHamUtil.isUntouchConstructable(getGraph(), potentialSet, potentialEdge);
    }
}
