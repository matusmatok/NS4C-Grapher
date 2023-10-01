package graph.builders;

import graph.components.Edge;
import graph.components.AbstractGraph;

public class UnavoidableBuilder extends AbstractTwoFactorBuilder{


    public UnavoidableBuilder(AbstractGraph graph) {
        super(graph);
    }

    public boolean canBuild(Edge potenatialEdge) {
        numberOfPositiveVertices = (int)graph.getVertices().stream().filter((vertex -> vertex.getWeight() == 1)).count();
        excludeEdge(potenatialEdge);
        final boolean result = canBuildConsistentTwoFactor();
        undoExcludeEdge(potenatialEdge);
        return result;
    }

}
