package graph.builders;

import graph.components.Edge;
import graph.components.Graph;
import graph.components.Vertex;
import graph.components.Vertex.ConsistentTwoFactorState;
import graph.utils.AbstractUnionFindSet;
import graph.utils.CountingUnionFindSet;
import lombok.NonNull;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

public class UnavoidableBuilder extends AbstractTwoFactorBuilder{


    public UnavoidableBuilder(Graph graph) {
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
