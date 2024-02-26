package graph.processors.ham;

import graph.components.Edge;
import graph.components.NoLadderC4ECGraph;
import graph.preprocessors.HamEdgeGetter;
import graph.processors.AbstractProcessor;

import java.util.List;

public class HamEdgeProcessor extends AbstractProcessor {
    public HamEdgeProcessor(NoLadderC4ECGraph graph, HamEdgeGetter potentialEdgeGetter) {
        super(graph, potentialEdgeGetter, null, null);
    }

    @Override
    public boolean hasProperty() {
        final List<Edge> potentiallyGoodEdges = potentialEdgeGetter.getResult();
        if (!potentiallyGoodEdges.isEmpty()) {
            for (final Edge edge : potentiallyGoodEdges) {
                System.out.println("GOOD EDGE " + edge.getId());
            }
            graph.print();
            return true;
        }
        return false;
    }
}
