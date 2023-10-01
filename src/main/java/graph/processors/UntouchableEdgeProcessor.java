package graph.processors;

import graph.builders.UntouchableBuilder;
import graph.components.AbstractGraph;
import graph.preprocessors.HamUntouchableEdgeGetter;
import graph.preprocessors.UntouchableAmbiguousVertexGetter;

public class UntouchableEdgeProcessor extends AbstractProcessor {

    public UntouchableEdgeProcessor(AbstractGraph graph) {
        super(graph, new HamUntouchableEdgeGetter(graph), new UntouchableAmbiguousVertexGetter(graph),
                new UntouchableBuilder(graph));
    }
}
