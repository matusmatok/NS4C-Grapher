package graph.preprocessors;

import graph.components.Edge;
import graph.components.Graph;
import graph.components.Vertex;
import graph.utils.AbstractUnionFindSet;
import graph.utils.SimpleUnionFindSet;
import java.util.List;

public abstract class HamEdgeGetter extends AbstractPreProcessor<Edge> {

    public HamEdgeGetter(Graph g) {
        super(g, new SimpleUnionFindSet());
    }

    @Override
    protected boolean canCloseCycle(Vertex color) {
        return color.getSubtreeSize() == graph.getVertices().size();
    }

    public abstract List<Edge> getResult();
}
