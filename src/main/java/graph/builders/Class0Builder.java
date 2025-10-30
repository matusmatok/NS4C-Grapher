package graph.builders;

import graph.components.AbstractGraph;
import graph.components.Edge;

public class Class0Builder extends AbstractTwoFactorBuilder {

    public Class0Builder(AbstractGraph graph) {
        super(graph);
    }

    @Override
    public boolean canBuild(Edge potenatialEdge) {
        numberOfPositiveVertices = (int)graph.getVertices().stream().filter((vertex -> vertex.getWeight() == 1)).count();

        excludeEdge(potenatialEdge);
        boolean result = canBuildConsistentTwoFactor();
        undoExcludeEdge(potenatialEdge);

        if (result) return true;

        unionFindSet.union(potenatialEdge.getVFrom(), potenatialEdge.getVTo());
        includeEdge(potenatialEdge);
        result = canBuildConsistentTwoFactor();
        undoIncludeEdge(potenatialEdge);
        unionFindSet.disunion();

        return result;
    }
}
