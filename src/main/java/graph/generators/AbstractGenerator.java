package graph.generators;

import graph.components.Graph;
import graph.processors.AbstractProcessor;

public abstract class AbstractGenerator {

    protected final AbstractProcessor processor;
    protected final Graph graph;
    protected final int depth;

    public AbstractGenerator(Graph graph, AbstractProcessor processor, int depth) {
        this.processor = processor;
        this.graph = graph;
        this.depth = depth;
    }

    public abstract boolean verifyTwoFactors();
    public abstract void generate();
}
