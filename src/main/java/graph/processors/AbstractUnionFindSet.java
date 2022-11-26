package graph.processors;

import graph.components.Vertex;
import lombok.AllArgsConstructor;

import java.util.Stack;

public abstract class AbstractUnionFindSet {
    @AllArgsConstructor
    protected class LogEntry {
        protected final Vertex root;
        protected final Vertex subordinate;
        protected final int rootFormerSubtreeDepth;
    }
    protected final Stack<LogEntry> unionLog = new Stack<>();

    public abstract void union(Vertex element1, Vertex element2);

    public abstract void disunion();

    public abstract Vertex find(Vertex element);
}
