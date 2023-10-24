package graph.generators;

import graph.components.NoLadderC4ECGraph;
import graph.components.SimpleGraph;
import graph.processors.AbstractProcessor;

public class NoLadderC4ECGenerator {

    private final NoLadderC4ECGraph graph;
    private final AbstractProcessor processor;
    private final int depth;

    public NoLadderC4ECGenerator(NoLadderC4ECGraph graph, AbstractProcessor processor, int depth) {
        this.graph = graph;
        this.processor = processor;
        this.depth = depth;
    }

    public boolean verifyTwoFactors() {
        return false;
    }

    public void generate() {
        generate(graph.getVertices().size());
    }

    private void generate(final int currentDepth) {
        if (depth <= currentDepth) {
            verifyTwoFactors();
        }


    }
}
