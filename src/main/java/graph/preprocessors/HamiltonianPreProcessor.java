package graph.preprocessors;

import graph.components.AbstractGraph;
import graph.components.Edge;
import graph.components.Vertex;
import graph.utils.AbstractUnionFindSet;

public class HamiltonianPreProcessor extends AbstractPreProcessor{

    public HamiltonianPreProcessor(AbstractGraph g, AbstractUnionFindSet unionFindSet) {
        super(g, unionFindSet);
    }

    @Override
    protected boolean canCloseCycle(Vertex color) {
        return color.getSubtreeSize() == graph.getVertices().size();
    }

    public boolean isHamiltonian() {
        final Edge randomEdge = graph.getEdge(0);
        includeEdge(randomEdge);
        unionFindSet.union(randomEdge.getVFrom(), randomEdge.getVTo());
        if (!cycleConstructible()) {
            unionFindSet.disunion();
            undoIncludeEdge(randomEdge);

            excludeEdge(randomEdge);
            if (!cycleConstructible()) {
                undoExcludeEdge(randomEdge);
                return false;
            } else {
                undoExcludeEdge(randomEdge);
            }
        } else {
            unionFindSet.disunion();
            undoIncludeEdge(randomEdge);
        }
        return true;
    }
}
