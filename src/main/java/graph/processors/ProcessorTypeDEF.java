package graph.processors;

import graph.builders.UnavoidableBuilder;
import graph.components.Graph;
import graph.preprocessors.HamUnavoidableEdgeGetter;
import graph.preprocessors.UnavoidableAmbiguousVertexGetter;

public class ProcessorTypeDEF extends AbstractProcessorV2 {

    public ProcessorTypeDEF(Graph graph) {
        super(graph, new HamUnavoidableEdgeGetter(graph), new UnavoidableAmbiguousVertexGetter(graph),
                new UnavoidableBuilder(graph));
    }

}
