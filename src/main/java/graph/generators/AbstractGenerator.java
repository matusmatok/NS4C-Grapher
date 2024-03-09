package graph.generators;

import graph.components.SimpleGraph;
import graph.processors.Processor;

public abstract class AbstractGenerator {

    protected final Processor processor;
    protected final SimpleGraph graph;
    protected final int depth;

    public AbstractGenerator(SimpleGraph graph, Processor processor, int depth) {
        this.processor = processor;
        this.graph = graph;
        this.depth = depth;
    }

    public abstract boolean verifyTwoFactors();
    public abstract void generate();
}
