package graph.processors;

import graph.builders.UnavoidableBuilder;
import graph.components.AbstractGraph;
import graph.preprocessors.UnavoidableAmbiguousVertexGetter;
import graph.preprocessors.HamUnavoidableEdgeGetter;

public class UnavoidableEdgeProcessor extends AbstractProcessor {

    public UnavoidableEdgeProcessor(AbstractGraph graph) {
        super(graph, new HamUnavoidableEdgeGetter(graph), new UnavoidableAmbiguousVertexGetter(graph),
                new UnavoidableBuilder(graph));
    }
}
