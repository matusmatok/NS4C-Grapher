package graph.utils;

import graph.components.Vertex;
import lombok.AllArgsConstructor;

import java.util.Stack;

public class CountingUnionFindSet implements AbstractUnionFindSet {

    @AllArgsConstructor
    protected class LogEntry {
        protected final Vertex root;
        protected final Vertex subordinate;
        protected final int rootFormerSubtreeDepth;
        protected final int rootFormerWeight;
    }

    private final Stack<LogEntry> unionLog = new Stack<>();

    @Override
    public void union(Vertex element1, Vertex element2) {
        if (element1.getParent() != null || element2.getParent() != null) throw new RuntimeException("Trying to unify two non-root elements.");
        if (element1.getSubtreeDepth() > element2.getSubtreeDepth()) {
            element2.setParent(element1);
            unionLog.push(new CountingUnionFindSet.LogEntry(element1, element2, element1.getSubtreeDepth(), element1.getWeight()));
            element1.setSubtreeSize(element1.getSubtreeSize() + element2.getSubtreeSize());
            element1.setWeight(element1.getWeight() + element2.getWeight());
        }
        else if (element2.getSubtreeDepth() > element1.getSubtreeDepth()) {
            element1.setParent(element2);
            unionLog.push(new CountingUnionFindSet.LogEntry(element2, element1, element2.getSubtreeDepth(), element2.getWeight()));
            element2.setSubtreeSize(element1.getSubtreeSize() + element2.getSubtreeSize());
            element2.setWeight(element1.getWeight() + element2.getWeight());
        }
        else {
            element2.setParent(element1);
            unionLog.push(new CountingUnionFindSet.LogEntry(element1, element2, element1.getSubtreeDepth(), element1.getWeight()));
            element1.setSubtreeDepth(element1.getSubtreeDepth() + 1);
            element1.setSubtreeSize(element1.getSubtreeSize() + element2.getSubtreeSize());
            element1.setWeight(element1.getWeight() + element2.getWeight());
        }
    }

    @Override
    public void disunion() {
        final CountingUnionFindSet.LogEntry logEntry = unionLog.pop();
        logEntry.root.setSubtreeDepth(logEntry.rootFormerSubtreeDepth);
        logEntry.subordinate.setParent(null);
        logEntry.root.setSubtreeSize(logEntry.root.getSubtreeSize() - logEntry.subordinate.getSubtreeSize());
        logEntry.root.setWeight(logEntry.rootFormerWeight);
    }
}
