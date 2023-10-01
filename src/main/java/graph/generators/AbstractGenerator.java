package graph.generators;

import graph.components.AbstractGraph;
import graph.components.SimpleGraph;
import graph.processors.AbstractProcessor;

public abstract class AbstractGenerator {

    protected final AbstractProcessor processor;
    protected final SimpleGraph graph;
    protected final int depth;

    public AbstractGenerator(SimpleGraph graph, AbstractProcessor processor, int depth) {
        this.processor = processor;
        this.graph = graph;
        this.depth = depth;
    }

    public abstract boolean verifyTwoFactors();
    public abstract void generate();
}
