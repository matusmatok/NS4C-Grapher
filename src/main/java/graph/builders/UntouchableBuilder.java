package graph.builders;

import graph.components.Edge;
import graph.components.Graph;

public class UntouchableBuilder extends AbstractTwoFactorBuilder {

    public UntouchableBuilder(Graph graph) {
        super(graph);
    }

    @Override
    public boolean canBuild(Edge potenatialEdge) {
        numberOfPositiveVertices = (int)graph.getVertices().stream().filter((vertex -> vertex.getWeight() == 1)).count();
        includeEdge(potenatialEdge);
        final boolean result = canBuildConsistentTwoFactor();
        undoIncludeEdge(potenatialEdge);
        return result;
    }
}
