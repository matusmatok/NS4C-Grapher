package graph.processors;

import graph.builders.UntouchableBuilder;
import graph.components.Graph;
import graph.preprocessors.HamUntouchableEdgeGetter;
import graph.preprocessors.UntouchableAmbiguousVertexGetter;

public class UntouchableEdgeProcessor extends AbstractProcessor {

    public UntouchableEdgeProcessor(Graph graph) {
        super(graph, new HamUntouchableEdgeGetter(graph), new UntouchableAmbiguousVertexGetter(graph),
                new UntouchableBuilder(graph));
    }
    @Override
    public boolean hasProperty() {
        return false;
    }
}
